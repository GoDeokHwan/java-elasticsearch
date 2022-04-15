package io.com.elastic.api.event;

import io.com.elastic.api.entity.Boards;
import org.springframework.context.ApplicationEvent;

import java.util.Optional;

public class BoardsModifyEvent extends ApplicationEvent {
    public BoardsModifyEvent(Boards boards) {
        super(boards);
    }

    public Optional<Boards> getBoards() {
        return Optional.ofNullable(getSource()).map(s -> (Boards) s);
    }
}
