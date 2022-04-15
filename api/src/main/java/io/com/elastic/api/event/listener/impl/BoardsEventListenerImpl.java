package io.com.elastic.api.event.listener.impl;

import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.entity.Users;
import io.com.elastic.api.event.BoardsCreateEvent;
import io.com.elastic.api.event.BoardsModifyEvent;
import io.com.elastic.api.event.UsersModifyEvent;
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

    private final BoardsIndexerService boardsIndexerService;

    @Override
    public void handleBoardsCreateEvent(BoardsCreateEvent event) {
        event.getBoards().map(Boards::convertToESBoards).ifPresent(b -> {
            boardsIndexerService.asyncProcessIndexingData(b, IndexingMessage.of(IndexingType.BOARDS, DataChangeType.CREATE, b), System.currentTimeMillis());
        });
    }

    @Override
    public void handleBoardsModifyEvent(BoardsModifyEvent event) {
        event.getBoards().map(Boards::convertToESBoards).ifPresent(b -> {
            boardsIndexerService.asyncProcessIndexingData(b, IndexingMessage.of(IndexingType.BOARDS, DataChangeType.UPDATE, b), System.currentTimeMillis());
        });
    }

    @Override
    public void handleUsersModifyEvent(UsersModifyEvent event) {
        event.getUsers().map(Users::convertToEsBoards).ifPresent(b -> {
            boardsIndexerService.asyncProcessIndexingData(b,
                    IndexingMessage.of(IndexingType.BOARDS_QUERY, DataChangeType.UPDATE, b),
                    System.currentTimeMillis());
        });
    }
}
