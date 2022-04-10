package io.com.elastic.api.service;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.dto.UsersDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class BoardsServiceTest {

    @Autowired
    private BoardsService boardsService;

    @Test
    public void 게시판_생성() {
        BoardsDTO boardsDTO = BoardsDTO.of(null, "TEST", "TESTTEST", LocalDateTime.now(), null, 1l);
        boardsService.createBoards(boardsDTO);
    }
}
