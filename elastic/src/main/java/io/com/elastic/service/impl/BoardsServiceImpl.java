package io.com.elastic.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.elastic.client.indexer.BoardsIndexer;
import io.com.elastic.client.indexer.dto.BoardsUpdateType;
import io.com.elastic.client.indexer.dto.IndexingResult;
import io.com.elastic.entity.boards.Boards;
import io.com.elastic.service.BoardsService;
import io.com.elastic.service.dto.common.IndexingMessage;
import io.com.elastic.service.dto.common.IndexingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {

    private final BoardsIndexer boardsIndexer;
    private final ObjectMapper objectMapper;

    @Override
    public boolean asyncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp) {
        IndexingResult result = IndexingResult.ofFailure();
        try {
            result = syncProcessIndexingData(boards, indexingMessage, sentTimestamp);
        } catch (IOException e) {
            log.warn("[ChatRooms] asyncProcessIndexingData error Id : {}", Optional.ofNullable(boards).map(Boards::getId).orElse(null));
        }
        return result.succed();
    }

    @Override
    public IndexingResult syncProcessIndexingData(Boards boards, IndexingMessage indexingMessage, Long sentTimestamp) throws IOException {
        IndexingResult result = IndexingResult.ofFailure();
        switch (indexingMessage.getDataChangeType()) {
            case UPDATE:
                result = upsert(boards, sentTimestamp, indexingMessage.getIndexingType());
                break;
            case CREATE:
            case INDEX:
                result = insert(boards, sentTimestamp);
                break;
        }
        return result;
    }

    private IndexingResult insert(Boards boards, Long sentTimestamp) throws IOException {
        return boardsIndexer.insert(boards, sentTimestamp);
    }

    private IndexingResult upsert(Boards boards, Long sentTimestamp, IndexingType indexingType) throws IOException {
        switch (indexingType) {
            default:
                return boardsIndexer.upsert(boards, sentTimestamp, DocWriteRequest.OpType.UPDATE, BoardsUpdateType.UPDATE_BOARDS);
        }
    }
}
