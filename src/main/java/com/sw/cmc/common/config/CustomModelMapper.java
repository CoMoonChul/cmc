package com.sw.cmc.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.sw.cmc.common.config
 * fileName       : CustomModelMapper
 * author         : ihw
 * date           : 2025. 2. 4.
 * description    : modelMapper configuration
 */
@Configuration
public class CustomModelMapper {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}