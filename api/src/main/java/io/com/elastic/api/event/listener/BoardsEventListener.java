package io.com.elastic.api.event.listener;

import io.com.elastic.api.event.BoardsCreateEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public interface BoardsEventListener {

    @TransactionalEventListener
    void handleBoardsCreateEvent(BoardsCreateEvent event);
}
