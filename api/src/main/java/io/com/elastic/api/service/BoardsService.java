package io.com.elastic.api.service;

import io.com.elastic.api.dto.BoardsDTO;

import java.util.List;

public interface BoardsService {
    void createBoards(BoardsDTO boards);

    List<BoardsDTO> getAll(String title, String comment);

    void modify(Long id, BoardsDTO request);

    BoardsDTO get(Long id);
}
