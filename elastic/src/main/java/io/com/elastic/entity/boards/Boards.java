package io.com.elastic.entity.boards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Boards {
    private Long id;
    private String title;
    private String comment;
    private Users users;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long lastIndexedTimeStamp;
}
