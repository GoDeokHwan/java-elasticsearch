package io.com.elastic.api.event;

import io.com.elastic.api.entity.Boards;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Optional;

@Getter
public class BoardsCreateEvent extends ApplicationEvent {

    public BoardsCreateEvent(Boards boards) {
        super(boards);
    }

    public Optional<Boards> getBoards() {
        return Optional.ofNullable(source).map(s -> (Boards) s);
    }
}
