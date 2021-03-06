package io.com.elastic.api.controller;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.service.BoardsService;
import io.com.elastic.api.utils.PageResponce;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public PageResponce<BoardsDTO> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        return boardsService.getAll(title, comment, size, page);
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
