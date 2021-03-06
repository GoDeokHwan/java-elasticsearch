package io.com.elastic.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@ConfigurationPropertiesScan("io.com.elastic")
@EnableCaching(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = {
        "io.com.elastic.api"
        , "io.com.elastic.core"
})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
