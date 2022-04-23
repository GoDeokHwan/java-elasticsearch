package io.com.elastic.api.dto;

import io.com.elastic.api.entity.Boards;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class BoardsDTO {
    protected Long id;
    protected String title;
    protected String comment;
    protected LocalDateTime createDate;
    protected LocalDateTime modifyDate;
    protected UsersDTO users;

    public Boards convertBoards() {
        Boards instance = new Boards();
        instance.setTitle(this.getTitle());
        instance.setComment(this.getComment());
        instance.setCreateDate(LocalDateTime.now());
        return instance;
    }

    public static BoardsDTO of(io.com.elastic.core.entity.Boards boards) {
        BoardsDTO instance = new BoardsDTO();
        instance.id = boards.getId();
        instance.title = boards.getTitle();
        instance.comment = boards.getComment();
        instance.createDate = boards.getCreateDate();
        instance.modifyDate = boards.getModifyDate();
        instance.users = UsersDTO.of(boards.getUsers());
        return instance;
    }
}
