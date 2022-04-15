package io.com.elastic.core.config.binder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "data-es")
public class DataEsProperties {
    private String hosts;
    private Integer port;
    private String indexPostfix;
    private String elasticCloudId;
    private String elasticCloudPW;
    private Indecies indecies;
    private Httpclient httpclient;

    public String getBoardsIndexName() {
        return ofNullable(this.indecies)
                .map(Indecies::getBoardsIndexName)
                .orElseGet(() -> "boards");
    }

    public HttpHost[] getDefaultHttpHost() {
        return ofNullable(hosts).map(host -> new HttpHost[]{new HttpHost(host, port)}).get();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "indecies")
    private class Indecies {
        protected String boardsIndexName;
    }

    @Getter
    @Setter
    @ToString
    @ConfigurationProperties(prefix = "httpclient")
    public static class Httpclient {

        private Pooling pooling = new Pooling();
        private int defaultReadTimeout;
        private int defaultConnectionTimeout;

        @Getter
        @Setter
        @ToString
        public static class Pooling {
            private int maxTotal;
            private int defaultMaxPerRoute;
        }
    }

}
