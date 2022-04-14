package io.com.elastic.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Import(value = {DataElasticSearchConfiguration.class})
@ComponentScan(basePackages = "io.com.elastic")
@EnableAsync
@Configuration
public class IndexerConfiguration {
}
