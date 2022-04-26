package io.com.elastic.core.client.searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SearchApiResponse<T> {
    private long size;
    private boolean hasMore;
    private List<T> data;
}
