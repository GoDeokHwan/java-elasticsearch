package io.com.elastic.api.config.binder;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "http")
public class HttpProperties {

    private int readTimeout;
    private int connectTimeout;
    private int maxTotal;
    private int defaultMaxPerRoute;

}
