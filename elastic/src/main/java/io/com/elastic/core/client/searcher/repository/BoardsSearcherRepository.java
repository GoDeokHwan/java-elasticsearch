package io.com.elastic.core.client.searcher.repository;

import io.com.elastic.core.client.searcher.dto.SearchApiResponse;
import io.com.elastic.core.entity.Boards;

import java.io.IOException;

public interface BoardsSearcherRepository {
    SearchApiResponse<Boards> findAllByTitleAndComment(String title, String comment, Integer page, Integer size) throws IOException;
}
