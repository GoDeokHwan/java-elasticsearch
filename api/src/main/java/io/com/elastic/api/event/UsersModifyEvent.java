package io.com.elastic.api.event;

import io.com.elastic.api.entity.Users;
import org.springframework.context.ApplicationEvent;

import java.util.Optional;

public class UsersModifyEvent extends ApplicationEvent {
    public UsersModifyEvent(Users users) {
        super(users);
    }

    public Optional<Users> getUsers() {
        return Optional.ofNullable(getSource())
                .map(s -> (Users) s);
    }
}
