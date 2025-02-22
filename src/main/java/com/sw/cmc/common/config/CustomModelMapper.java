package com.sw.cmc.common.config;

import com.sw.cmc.domain.comment.CommentDomain;
import com.sw.cmc.entity.Comment;
import org.modelmapper.AbstractConverter;
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
        ModelMapper modelMapper = new ModelMapper();

        // Comment -> CommentDomain 변환 Converter
        modelMapper.addConverter(new AbstractConverter<Comment, CommentDomain>() {
            @Override
            protected CommentDomain convert(Comment source) {
                return CommentDomain.builder()
                        .commentId(source.getCommentId())
                        .content(source.getContent())
                        .userNum(source.getUser().getUserNum())
                        .targetId(source.getTargetId())
                        .commentTarget(source.getCommentTarget())
                        .createdAt(source.getCreatedAt())
                        .updatedAt(source.getUpdatedAt())
                        .userName(source.getUser().getUsername())
                        .email(source.getUser().getEmail())
                        .build();
            }
        });

        return modelMapper;
    }
}