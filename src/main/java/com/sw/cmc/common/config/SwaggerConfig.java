package com.sw.cmc.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : SwaggerConfig
 * author         : ihw
 * date           : 2025. 2. 19.
 * description    : swagger config
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Cmc", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
