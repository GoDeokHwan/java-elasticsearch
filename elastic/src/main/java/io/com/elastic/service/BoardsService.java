package io.com.elastic.service;

import io.com.elastic.client.indexer.dto.IndexingResult;
import io.com.elastic.entity.boards.Boards;
import io.com.elastic.service.dto.common.IndexingMessage;

import java.io.IOException;

public interface BoardsService {
    boolean asyncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp);

    IndexingResult syncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp) throws IOException;
}
