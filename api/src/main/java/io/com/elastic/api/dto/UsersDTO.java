package io.com.elastic.api.dto;

import io.com.elastic.api.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UsersDTO {
    protected Long id;
    protected String name;
    protected String email;

    public Users convertUsers() {
        Users instance = new Users();
        instance.setName(this.getName());
        instance.setEmail(this.getEmail());
        return instance;
    }

    public static UsersDTO of(io.com.elastic.core.entity.Users users) {
        UsersDTO instance = new UsersDTO();
        instance.id = users.getId();
        instance.name = users.getName();
        instance.email = users.getEmail();
        return instance;
    }
}
