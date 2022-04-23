package io.com.elastic.core.client.searcher.impl;

import io.com.elastic.core.client.searcher.BoardsSearcher;
import io.com.elastic.core.client.searcher.dto.SearchApiResponse;
import io.com.elastic.core.client.searcher.repository.BoardsSearcherRepository;
import io.com.elastic.core.entity.Boards;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardsSearcherImpl implements BoardsSearcher {

    private final BoardsSearcherRepository boardsSearcherRepository;

    @Override
    public SearchApiResponse<Boards> findAllByTitleAndComment(String title, String comment, Integer page, Integer size) {
        try {
            return boardsSearcherRepository.findAllByTitleAndComment(title, comment, page, size);
        } catch (IOException e) {
            e.printStackTrace();
            return new SearchApiResponse<>();
        }
    }
}
