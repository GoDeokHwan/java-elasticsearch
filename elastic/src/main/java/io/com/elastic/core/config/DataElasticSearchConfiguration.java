package io.com.elastic.core.config;

import io.com.elastic.core.config.binder.DataEsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Slf4j
@ComponentScan
@EnableConfigurationProperties({DataEsProperties.class})
@Configuration
@RequiredArgsConstructor
public class DataElasticSearchConfiguration extends AbstractElasticsearchConfiguration {
    private final DataEsProperties dataEsProperties;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(dataEsProperties.getHosts())
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
