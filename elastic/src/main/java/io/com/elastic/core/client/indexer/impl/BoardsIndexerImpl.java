package io.com.elastic.core.client.indexer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.elastic.core.client.indexer.BoardsIndexer;
import io.com.elastic.core.client.indexer.dto.BoardsUpdateType;
import io.com.elastic.core.client.indexer.dto.IndexingResult;
import io.com.elastic.core.client.worker.impl.ElasticSearchWorkerImpl;
import io.com.elastic.core.config.binder.DataEsProperties;
import io.com.elastic.core.entity.Boards;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;

@Slf4j
@Component
public class BoardsIndexerImpl extends ElasticSearchWorkerImpl implements BoardsIndexer {

    private static final String UPSERT_FAILURE_ERROR_MESSAGE_FORMAT = "Boards %s upsert failure. id : %s";
    private final ObjectMapper objectMapper;
    private final DataEsProperties properties;

    public BoardsIndexerImpl(DataEsProperties dataEsProperties, ObjectMapper objectMapper, DataEsProperties properties) {
        super(dataEsProperties);
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    @Override
    public IndexingResult insert(Boards boards, Long indexingTimestamp) throws IOException {
        boards.setLastIndexedTimeStamp(indexingTimestamp);
        RestHighLevelClient client = null;
        IndexResponse indexResponse = null;
        try {
            client = getHighLevelClient();
            IndexRequest request = getIndexRequest(boards);
            indexResponse = client.index(request, RequestOptions.DEFAULT);
        } catch (ConnectException e) {
            log.warn("upsert ConnectException ", e);
            throw e;
        } catch (IOException e) {
            log.error("upsert Exception", e);
            throw e;
        } finally {
            super.closeClient(client);
        }
        return IndexingResult.getUpsertIndexingResult(indexResponse, indexResponse.getId(), String.format(UPSERT_FAILURE_ERROR_MESSAGE_FORMAT, DocWriteRequest.OpType.CREATE, indexResponse.getId()));

    }

    private IndexRequest getIndexRequest(Object object) throws JsonProcessingException {
        IndexRequest request = new IndexRequest(properties.getBoardsIndexName());
        request.source(objectMapper.writeValueAsString(object), XContentType.JSON);
        log.info("[{}] = {}", BoardsUpdateType.UPDATE_BOARDS, objectMapper.writeValueAsString(object));
        return request;
    }

    @Override
    public IndexingResult upsert(Boards boards, Long indexingTimestamp, DocWriteRequest.OpType opType, BoardsUpdateType boardsUpdateType) throws IOException {
        return null;
    }

    @Override
    public IndexingResult upsertByQuery(Boards boards, Long indexingTimestamp, BoardsUpdateType boardsUpdateType, String queryFieldName, Object queryValue) throws IOException {
        return null;
    }
}
