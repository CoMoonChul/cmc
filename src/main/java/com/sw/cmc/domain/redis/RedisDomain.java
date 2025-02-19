package com.sw.cmc.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.redis
 * fileName       : RedisDomain
 * author         : 82104
 * date           : 2025-02-19
 * description    : redis domain
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisDomain {
    private String key;
    private String value;
}
