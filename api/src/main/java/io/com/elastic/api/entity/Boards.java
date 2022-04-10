package io.com.elastic.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @Column
    private LocalDateTime createDate;
    @Column
    private LocalDateTime modifyDate;

}
