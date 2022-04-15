package io.com.elastic.api.entity;

import io.com.elastic.api.dto.UsersDTO;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity(name = "users")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "users")
    private Set<Boards> boards = new HashSet<>();
    @Column
    private String name;
    @Column
    private String email;

    public UsersDTO convertUsersDTO() {
        UsersDTO instance = new UsersDTO();
        instance.setId(this.getId());
        instance.setName(this.getName());
        instance.setEmail(this.getEmail());
        return instance;
    }

    public void changeUsers(UsersDTO usersDTO) {
        if (Objects.nonNull(usersDTO.getName())) {
            this.name = usersDTO.getName();
        }
        if (Objects.nonNull(usersDTO.getEmail())) {
            this.email = usersDTO.getEmail();
        }
    }

    public io.com.elastic.core.entity.Boards convertToEsBoards() {
        io.com.elastic.core.entity.Boards instance = new io.com.elastic.core.entity.Boards();
        instance.setUsers(io.com.elastic.core.entity.Users.of(this.getId(), this.getName(), this.getEmail()));
        return instance;
    }
}
