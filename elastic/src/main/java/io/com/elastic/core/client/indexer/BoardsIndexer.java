package io.com.elastic.core.client.indexer;

import io.com.elastic.core.client.indexer.dto.BoardsUpdateType;
import io.com.elastic.core.client.indexer.dto.IndexingResult;
import io.com.elastic.core.entity.Boards;
import org.elasticsearch.action.DocWriteRequest;

import java.io.IOException;

public interface BoardsIndexer {
    IndexingResult insert(Boards boards, Long indexingTimestamp) throws IOException;

    IndexingResult upsert(Boards boards, Long indexingTimestamp, DocWriteRequest.OpType opType, BoardsUpdateType boardsUpdateType) throws IOException;

    IndexingResult upsertByQuery(Boards boards, Long indexingTimestamp, BoardsUpdateType boardsUpdateType, String queryFieldName, Object queryValue) throws IOException;

}
