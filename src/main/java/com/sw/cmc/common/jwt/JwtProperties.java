package com.sw.cmc.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : JwtProperties
 * author         : SungSuHan
 * date           : 2025-02-10
 * description    :
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final Token token = new Token();

    @Getter
    @Setter
    public static class Token {
        private String secretKey;
        private long accessTokenExpirationTimeInSeconds;
        private long refreshTokenExpirationTimeInSeconds;
    }

}
