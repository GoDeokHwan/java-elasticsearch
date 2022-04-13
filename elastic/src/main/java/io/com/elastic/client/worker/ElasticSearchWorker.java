package io.com.elastic.client.worker;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Closeable;
import java.net.UnknownHostException;

public interface ElasticSearchWorker {
    String DEFAULT_TYPE = "_doc";

    RestHighLevelClient getHighLevelClient() throws UnknownHostException;

    void closeClientNLog(Closeable client, SearchResponse searchResponse, SearchRequest searchRequest) throws UnknownHostException;

    void closeClient(Closeable client);
}
