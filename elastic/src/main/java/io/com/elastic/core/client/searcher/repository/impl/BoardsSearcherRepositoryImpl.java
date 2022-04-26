package io.com.elastic.core.client.searcher.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.elastic.core.client.searcher.dto.SearchApiResponse;
import io.com.elastic.core.client.searcher.repository.BoardsSearcherRepository;
import io.com.elastic.core.client.worker.impl.ElasticSearchWorkerImpl;
import io.com.elastic.core.config.binder.DataEsProperties;
import io.com.elastic.core.entity.Boards;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BoardsSearcherRepositoryImpl extends ElasticSearchWorkerImpl implements BoardsSearcherRepository {

    private final DataEsProperties properties;
    private final ObjectMapper objectMapper;

    private final static String TITLE_LABEL = "title";
    private final static String COMMENT_LABEL = "comment";
    private final static String CREATE_DATE_LABEL = "createDate";

    public BoardsSearcherRepositoryImpl(DataEsProperties dataEsProperties, DataEsProperties properties, ObjectMapper objectMapper) {
        super(dataEsProperties);
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public SearchApiResponse<Boards> findAllByTitleAndComment(String title, String comment, Integer page, Integer size) throws IOException {
        RestHighLevelClient client = null;
        try {
            client = getHighLevelClient();
            SearchRequest searchRequest = new SearchRequest(properties.getBoardsIndexName());
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // Query
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

            if (title != null && title != "") {
                boolQueryBuilder.must(QueryBuilders.matchQuery(TITLE_LABEL, title));
            }
            if (comment != null && comment != "") {
                boolQueryBuilder.must(QueryBuilders.matchQuery(COMMENT_LABEL, comment));
            }

            // 조합
            searchSourceBuilder.query(boolQueryBuilder)
                    .from((page-1)).size(size)  // 페이징
                    .sort(CREATE_DATE_LABEL, SortOrder.DESC);   // 정렬

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            Optional<SearchResponse> searchResponseOptional = Optional.ofNullable(searchResponse);
            SearchHit[] searchHits = searchResponseOptional.map(SearchResponse::getHits).map(SearchHits::getHits).orElseGet(() -> new SearchHit[0]);
            List<Boards> collect = Arrays.stream(searchHits).filter(s -> s.getSourceAsString() != null && s.getSourceAsString() != "").map(v -> convert(v.getSourceAsString())).collect(Collectors.toList());
            return SearchApiResponse.of(searchResponseOptional.map(SearchResponse::getHits).map(SearchHits::getTotalHits).map(v -> v.value).orElse(0L), collect.size() < searchHits.length, collect);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            super.closeClient(client);
        }
    }

    private Boards convert(String ordersString) {
        try {
            return objectMapper.readValue(ordersString, Boards.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
