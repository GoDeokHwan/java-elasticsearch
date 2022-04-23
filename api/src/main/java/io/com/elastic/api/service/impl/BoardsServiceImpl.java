package io.com.elastic.api.service.impl;

import io.com.elastic.api.dto.BoardsDTO;
import io.com.elastic.api.entity.Boards;
import io.com.elastic.api.event.BoardsCreateEvent;
import io.com.elastic.api.event.BoardsModifyEvent;
import io.com.elastic.api.repository.BoardsRepository;
import io.com.elastic.api.repository.UsersRepository;
import io.com.elastic.api.service.BoardsService;
import io.com.elastic.api.utils.PageResponce;
import io.com.elastic.core.client.searcher.BoardsSearcher;
import io.com.elastic.core.client.searcher.dto.SearchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
    private final BoardsRepository boardsRepository;
    private final UsersRepository usersRepository;
    private final BoardsSearcher boardsSearcher;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createBoards(BoardsDTO boardsDTO) {
        Boards boards = boardsDTO.convertBoards();
        boards.ofUsers(usersRepository.getById(boardsDTO.getUsers().getId()));
        boardsRepository.save(boards);
        publisher.publishEvent(new BoardsCreateEvent(boards));
    }

    @Override
    public PageResponce<List<BoardsDTO>> getAll(String title, String comment, Integer size, Integer page) {
        SearchApiResponse<io.com.elastic.core.entity.Boards> response = boardsSearcher.findAllByTitleAndComment(title, comment, page, size);
        List<BoardsDTO> result = response.getData().stream()
                .map(b -> BoardsDTO.of(b))
                .collect(Collectors.toList());

        return (PageResponce<List<BoardsDTO>>) PageResponce.of((int) (response.getSize()/size)
                , (int) response.getSize()
                , page
                , result
        );
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
