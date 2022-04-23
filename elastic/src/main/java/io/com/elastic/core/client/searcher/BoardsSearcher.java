package io.com.elastic.core.client.searcher;

import io.com.elastic.core.client.searcher.dto.SearchApiResponse;
import io.com.elastic.core.entity.Boards;

public interface BoardsSearcher {
    SearchApiResponse<Boards> findAllByTitleAndComment(String title, String comment, Integer page, Integer size);
}
