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
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Insert
     * */
    @Override
    public IndexingResult insert(Boards boards, Long indexingTimestamp) throws IOException {
        boards.setLastIndexedTimeStamp(indexingTimestamp);
        RestHighLevelClient client = null;
        IndexResponse indexResponse = null;
        try {
            client = getHighLevelClient();
            IndexRequest request = getIndexRequest(boards, boards.getId().toString());
            indexResponse = client.index(request, RequestOptions.DEFAULT);
        } catch (ConnectException e) {
            log.warn("upsert ConnectException ", e);
            throw e;
        } catch (IOException e) {
            log.error("upsert Exception", e);
            throw e;
        } catch (ElasticsearchException e) {
            log.error("upsert ElasticsearchException", e);
            throw e;
        } finally {
            super.closeClient(client);
        }
        return IndexingResult.getUpsertIndexingResult(indexResponse, indexResponse.getId(), String.format(UPSERT_FAILURE_ERROR_MESSAGE_FORMAT, DocWriteRequest.OpType.CREATE, indexResponse.getId()));

    }

    /**
     * Insert & Update
     *
     * */
    @Override
    public IndexingResult upsert(Boards boards, Long indexingTimestamp, DocWriteRequest.OpType opType, BoardsUpdateType boardsUpdateType) throws IOException {
        boards.setLastIndexedTimeStamp(indexingTimestamp);
        RestHighLevelClient client = null;
        UpdateResponse updateResponse;
        try {
            client = getHighLevelClient();
            UpdateRequest updateRequest = getProductUpsertRequest(boards, boardsUpdateType);
            updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (ConnectException e) {
            log.warn("upsert ConnectException ", e);
            throw e;
        } catch (IOException e) {
            log.error("upsert Exception", e);
            throw e;
        } catch (ElasticsearchException e) {
            log.error("upsert ElasticsearchException", e);
            throw e;
        } finally {
            super.closeClient(client);
        }
        return IndexingResult.getUpsertIndexingResult(updateResponse, String.valueOf(boards.getId()), String.format(UPSERT_FAILURE_ERROR_MESSAGE_FORMAT, opType.getLowercase(), boards.getId()));
    }

    /**
     * Query Update
     *
     * */
    @Override
    public IndexingResult upsertByQuery(Boards boards, Long indexingTimestamp, BoardsUpdateType boardsUpdateType, String queryFieldName, Object queryValue) throws IOException {
        boards.setLastIndexedTimeStamp(indexingTimestamp);
        RestHighLevelClient client = null;
        BulkByScrollResponse updateByQueryResponse = null;
        try {
            client = getHighLevelClient();
            UpdateByQueryRequest updateByQueryRequest = getProductUpsertByQueryRequest(boards, boardsUpdateType, queryFieldName, queryValue);
            updateByQueryResponse = client.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        } catch (ConnectException e) {
            log.warn("upsert Query ConnecException ", e);
        } catch (IllegalArgumentException e) {
            log.error("upsert IllegalArgumentException", e);
            throw e;
        } catch (IOException e) {
            log.error("upsert Exception", e);
            throw e;
        } finally {
            super.closeClient(client);
        }

        List<String> failedList = updateByQueryResponse.getBulkFailures().stream().map(BulkItemResponse.Failure::getId).collect(Collectors.toList());
        List<String> failMessages = updateByQueryResponse.getBulkFailures().stream().map(BulkItemResponse.Failure::getMessage).collect(Collectors.toList());
        return IndexingResult.of(String.join(" :: ", failMessages), failedList);
    }

    private UpdateByQueryRequest getProductUpsertByQueryRequest(Boards boards, BoardsUpdateType boardsUpdateType, String queryFieldName, Object queryValue) throws JsonProcessingException{
        return getUpdateByQueryRequest(boards, boardsUpdateType.getScriptName(), queryFieldName, queryValue);
    }

    private UpdateByQueryRequest getUpdateByQueryRequest(Object object, String scriptName, String queryFieldName, Object queryValue) throws JsonProcessingException {
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(properties.getBoardsIndexName());
        updateByQueryRequest.setConflicts("proceed");
        updateByQueryRequest.setQuery(new TermQueryBuilder(queryFieldName, queryValue));
        updateByQueryRequest.setScript(new Script(
                ScriptType.STORED,
                null,
                scriptName,
                XContentHelper.convertToMap(new BytesArray(objectMapper.writeValueAsString(object)), true, XContentType.JSON).v2()));
        log.info("## Boards ## {} : {}", scriptName, objectMapper.writeValueAsString(object));
        return updateByQueryRequest;
    }

    private UpdateRequest getProductUpsertRequest(Boards boards, BoardsUpdateType boardsUpdateType) throws JsonProcessingException {
        UpdateRequest updateRequest = getUpdateRequestNotRefresh(boards, String.valueOf(boards.getId()), boardsUpdateType.getScriptName(), true);
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        return updateRequest;
    }

    private UpdateRequest getUpdateRequestNotRefresh(Object object, String id, String scriptName, boolean scriptedUpsert) throws JsonProcessingException {
        UpdateRequest updateRequest = new UpdateRequest(properties.getBoardsIndexName(), id);
        log.info("## Boards ## {} : {}", scriptName, objectMapper.writeValueAsString(object));
        updateRequest.script(new Script(
                ScriptType.STORED,
                null,
                scriptName,
                XContentHelper.convertToMap(new BytesArray(objectMapper.writeValueAsString(object)), true, XContentType.JSON).v2()));
        updateRequest.retryOnConflict(3);
        if (scriptedUpsert) {
            updateRequest.scriptedUpsert(scriptedUpsert);
            updateRequest.upsert(getIndexRequest(object, id));
        }
        return updateRequest;
    }

    private IndexRequest getIndexRequest(Object object, String id) throws JsonProcessingException {
        IndexRequest request = new IndexRequest(properties.getBoardsIndexName(), DEFAULT_TYPE, id);
        request.source(objectMapper.writeValueAsString(object), XContentType.JSON);
        log.info("[{}] = {}", BoardsUpdateType.UPDATE_BOARDS, objectMapper.writeValueAsString(object));
        return request;
    }

}
