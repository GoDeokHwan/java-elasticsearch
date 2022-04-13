package io.com.elastic.client.worker.impl;

import com.google.common.io.Closeables;
import io.com.elastic.client.worker.ElasticSearchWorker;
import io.com.elastic.config.binder.DataEsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.net.UnknownHostException;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticSearchWorkerImpl implements ElasticSearchWorker {
    private final DataEsProperties dataEsProperties;

    @Override
    public RestHighLevelClient getHighLevelClient() throws UnknownHostException {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        dataEsProperties.getHosts()
                ).setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setMaxConnTotal(dataEsProperties.getHttpClientProperties().getPooling().getMaxTotal());
                    httpClientBuilder.setMaxConnPerRoute(dataEsProperties.getHttpClientProperties().getPooling().getDefaultMaxPerRoute());
                    httpClientBuilder.setDefaultCredentialsProvider(getDefaultCredentialProvider());
                    return httpClientBuilder;
                }).setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(dataEsProperties.getHttpClientProperties().getDefaultConnectionTimeout())
                        .setSocketTimeout(dataEsProperties.getHttpClientProperties().getDefaultReadTimeout())
                )
        );
        return restHighLevelClient;
    }

    public BasicCredentialsProvider getDefaultCredentialProvider() {
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(dataEsProperties.getElasticCloudId(), dataEsProperties.getElasticCloudPW()));

        return basicCredentialsProvider;
    }

    @Override
    public void closeClientNLog(Closeable client, SearchResponse searchResponse, SearchRequest searchRequest) throws UnknownHostException {
        ofNullable(searchResponse).map(SearchResponse::getTook)
                .map(TimeValue::getMillis)
                .filter(ms -> ms > 1000)
                .ifPresent(ms -> log.warn("Search Slow Query : [{}ms], {}", ms.toString(), searchRequest));
        closeClient(client);
    }

    @Override
    public void closeClient(Closeable client) {
        try {
            Closeables.close(client, true);
        } catch (Exception e) {
            log.error("closeClientNLog", e);
        }
    }
}
