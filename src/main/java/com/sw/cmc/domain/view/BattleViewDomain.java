package com.sw.cmc.domain.view;

import lombok.Builder;
import lombok.Getter;

/**
 * packageName    : com.sw.cmc.domain.view
 * fileName       : BattleViewDomain
 * author         : ihw
 * date           : 2025. 2. 14.
 * description    : 배틀 조회수 도메인
 */
@Getter
@Builder
public class BattleViewDomain {
    private Long battleId;
    private Long viewCount;
}
