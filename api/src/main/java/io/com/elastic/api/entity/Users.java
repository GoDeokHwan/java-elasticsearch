package io.com.elastic.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "users")
    private Set<Boards> boards = new HashSet<>();
    @Column
    private String name;
    @Column
    private String email;
}
