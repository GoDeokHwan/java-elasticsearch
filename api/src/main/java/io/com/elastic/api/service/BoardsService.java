package io.com.elastic.api.service;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.utils.PageResponce;

public interface BoardsService {
    void createBoards(BoardsDTO boards);

    PageResponce getAll(String title, String comment, Integer size, Integer page);

    void modify(Long id, BoardsDTO request);

    BoardsDTO get(Long id);
}
