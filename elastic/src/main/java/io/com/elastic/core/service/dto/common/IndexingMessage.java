package io.com.elastic.core.service.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor(staticName = "of")
public class IndexingMessage implements Serializable {
    private IndexingType indexingType;
    private DataChangeType dataChangeType;
    private Object payload;
}
