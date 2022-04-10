package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.repository.BoardsRepository;
import io.com.elastic.api.repository.UsersRepository;
import io.com.elastic.api.service.BoardsService;
import io.com.elastic.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
    private final BoardsRepository boardsRepository;
    private final UsersRepository usersRepository;

    @Override
    public void createBoards(BoardsDTO boardsDTO) {
        Boards boards = boardsDTO.convertBoards();
        boards.ofUsers(usersRepository.getById(boardsDTO.getUsers().getId()));
        boardsRepository.save(boards);
    }

    @Override
    public List<BoardsDTO> getAll(String title, String comment) {
        return boardsRepository.findAllByTitleAndComment(title, comment);
    }

    @Override
    public void modify(Long id, BoardsDTO boardsDTO) {
        Boards boards = boardsRepository.getById(id);
        boards.changeBoards(boardsDTO);
    }

    @Override
    public BoardsDTO get(Long id) {
        return Optional.ofNullable(boardsRepository.getById(id))
                .orElseGet(() -> new Boards())
                .convertBoards();
    }
}
