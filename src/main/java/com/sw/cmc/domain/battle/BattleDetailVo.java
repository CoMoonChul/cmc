package com.sw.cmc.domain.battle;

import com.sw.cmc.entity.Battle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleDetailVo
 * author         : ihw
 * date           : 2025. 3. 6.
 * description    : 배틀 상세 조회용 vo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BattleDetailVo {
    private Battle battle;
    private String username;
    private Long leftVote;
    private Long rightVote;
    private Long viewCount;
}
