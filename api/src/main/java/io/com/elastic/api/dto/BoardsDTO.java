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
}
