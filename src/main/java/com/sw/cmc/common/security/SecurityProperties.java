package com.sw.cmc.common.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : SecurityProperties
 * author         : SungSuHan
 * date           : 2025-02-19
 * description    : API 화이트리스트 설정
 */
@Component
@ConfigurationProperties(prefix = "security")
@Getter
public class SecurityProperties {
    private final List<String> authorizationWhitelist = new ArrayList<>();
}
