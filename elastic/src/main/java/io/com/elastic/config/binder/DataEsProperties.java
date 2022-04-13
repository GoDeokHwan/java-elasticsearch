package io.com.elastic.config.binder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "data-es")
public class DataEsProperties {
    private String hosts;
    private String port;
    private String indexPostfix;
    private String elasticCloudId;
    private String elasticCloudPW;
    private Indecies indecies;
    private HttpClientProperties httpClientProperties;

    public String getBoardsIndexName() {
        return Optional.ofNullable(this.indecies)
                .map(Indecies::getBoardsIndexName)
                .orElseGet(() -> "boards");
    }

    @Getter
    @Setter
    private class Indecies {
        protected String boardsIndexName;
    }
}
