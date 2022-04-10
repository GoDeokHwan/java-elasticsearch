package io.com.elastic.api.entity;

import io.com.elastic.api.dto.BoardsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity(name = "boards")
@DynamicUpdate
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String comment;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
    @Column
    private LocalDateTime createDate;
    @Column
    private LocalDateTime modifyDate;

    public void changeBoards(BoardsDTO boardsDTO) {
        if (Objects.nonNull(boardsDTO.getTitle())) {
            this.title = boardsDTO.getTitle();
        }
        if (Objects.nonNull(boardsDTO.getComment())) {
            this.comment = boardsDTO.getComment();
        }
        this.modifyDate = LocalDateTime.now();
    }

    public BoardsDTO convertBoards() {
        return BoardsDTO.of(id, title, comment, createDate, modifyDate, users.convertUsersDTO());
    }

    public void ofUsers(Users users) {
        this.users = users;
    }
}
