package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.event.BoardsCreateEvent;
import io.com.elastic.api.event.BoardsModifyEvent;
import io.com.elastic.api.repository.BoardsRepository;
import io.com.elastic.api.repository.UsersRepository;
import io.com.elastic.api.service.BoardsService;
import io.com.elastic.api.service.UsersService;
import io.com.elastic.core.service.BoardsIndexerService;
import io.com.elastic.core.service.dto.common.DataChangeType;
import io.com.elastic.core.service.dto.common.IndexingMessage;
import io.com.elastic.core.service.dto.common.IndexingType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;

    @Override
    public void createBoards(BoardsDTO boardsDTO) {
        Boards boards = boardsDTO.convertBoards();
        boards.ofUsers(usersRepository.getById(boardsDTO.getUsers().getId()));
        boardsRepository.save(boards);
        publisher.publishEvent(new BoardsCreateEvent(boards));
    }

    @Override
    public List<BoardsDTO> getAll(String title, String comment) {
        return boardsRepository.findAllByTitleAndComment(title, comment);
    }

    @Override
    public void modify(Long id, BoardsDTO boardsDTO) {
        Boards boards = boardsRepository.getById(id);
        boards.changeBoards(boardsDTO);
        publisher.publishEvent(new BoardsModifyEvent(boards));
    }


    @Override
    public BoardsDTO get(Long id) {
        return Optional.ofNullable(boardsRepository.getById(id))
                .orElseGet(() -> new Boards())
                .convertBoards();
    }
}
