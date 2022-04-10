package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.repository.BoardsRepository;
import io.com.elastic.api.service.BoardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
    private final BoardsRepository boardsRepository;

    @Override
    public void createBoards(BoardsDTO boardsDTO) {
        Boards boards = boardsDTO.convertBoards();
        boardsRepository.save(boards);
    }
}
