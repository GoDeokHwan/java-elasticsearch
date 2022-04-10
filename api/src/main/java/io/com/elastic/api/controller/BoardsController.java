package io.com.elastic.api.controller;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.service.BoardsService;
import io.com.elastic.entity.boards.Boards;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping("")
    public String create(
            @RequestBody BoardsDTO request
    ) {
        boardsService.createBoards(request);
        return "OK";
    }

    @GetMapping("")
    public List<BoardsDTO> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String comment
    ) {
        return boardsService.getAll(title, comment);
    }

    @PatchMapping("/{id}")
    public String modify(
            @PathVariable Long id,
            @RequestBody BoardsDTO request
    ) {
        boardsService.modify(id, request);
        return "OK";
    }

    @GetMapping("/{id}")
    public BoardsDTO get(
            @PathVariable Long id
    ) {
        return boardsService.get(id);
    }
}
