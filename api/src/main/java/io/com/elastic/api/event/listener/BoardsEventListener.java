package io.com.elastic.api.event.listener;

import io.com.elastic.api.event.BoardsCreateEvent;
import io.com.elastic.api.event.BoardsModifyEvent;
import io.com.elastic.api.event.UsersModifyEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public interface BoardsEventListener {

    @TransactionalEventListener
    void handleBoardsCreateEvent(BoardsCreateEvent event);

    @TransactionalEventListener
    void handleBoardsModifyEvent(BoardsModifyEvent event);

    @TransactionalEventListener
    void handleUsersModifyEvent(UsersModifyEvent event);
}
