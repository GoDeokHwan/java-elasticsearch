package io.com.elastic.core.client.indexer.dto;

import lombok.Getter;

@Getter
public enum BoardsUpdateType {
    UPDATE_BOARDS("update-boards");

    private final String scriptName;

    BoardsUpdateType(String scriptName) {
        this.scriptName = scriptName;
    }

}
