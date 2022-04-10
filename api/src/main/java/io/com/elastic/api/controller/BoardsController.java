package io.com.elastic.api.controller;

import io.com.elastic.api.service.BoardsService;
import io.com.elastic.entity.boards.Boards;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;

    public String create() {
        
        return null;
    }
}
