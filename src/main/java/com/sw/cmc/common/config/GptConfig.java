package com.sw.cmc.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : GptConfig
 * author         : ihw
 * date           : 2025. 2. 19.
 * description    : gpt configuration
 */
@Configuration
@Getter
public class GptConfig {
    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.url.temp}")
    private String promtUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
