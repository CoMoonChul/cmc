package com.sw.cmc.domain.ai;

import lombok.*;

/**
 * packageName    : com.sw.cmc.domain.ai
 * fileName       : AIDomain
 * author         : ihw
 * date           : 2025. 4. 29.
 * description    :
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIDomain {
    private Long reviewId;
    private String title;
    private String content;
    private String codeContent;
    private String codeType;
}
