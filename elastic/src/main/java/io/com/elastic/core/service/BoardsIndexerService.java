package io.com.elastic.core.service;

import io.com.elastic.core.client.indexer.dto.IndexingResult;
import io.com.elastic.core.entity.Boards;
import io.com.elastic.core.service.dto.common.IndexingMessage;

import java.io.IOException;

public interface BoardsIndexerService {
    boolean asyncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp);

    IndexingResult syncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp) throws IOException;
}
