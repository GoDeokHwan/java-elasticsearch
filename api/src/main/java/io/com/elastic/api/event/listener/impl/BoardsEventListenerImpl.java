package io.com.elastic.api.event.listener.impl;

import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.event.BoardsCreateEvent;
import io.com.elastic.api.event.listener.BoardsEventListener;
import io.com.elastic.core.service.BoardsIndexerService;
import io.com.elastic.core.service.dto.common.DataChangeType;
import io.com.elastic.core.service.dto.common.IndexingMessage;
import io.com.elastic.core.service.dto.common.IndexingType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardsEventListenerImpl implements BoardsEventListener {

    private final BoardsIndexerService boardsService;

    @Override
    public void handleBoardsCreateEvent(BoardsCreateEvent event) {
        event.getBoards().map(Boards::convertToESBoards).ifPresent(b -> {
            boardsService.asyncProcessIndexingData(b, IndexingMessage.of(IndexingType.BOARDS, DataChangeType.CREATE, b), System.currentTimeMillis());
        });
    }
}
