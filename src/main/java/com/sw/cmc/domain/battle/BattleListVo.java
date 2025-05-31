package com.sw.cmc.domain.battle;

import com.sw.cmc.entity.Battle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleListVo
 * author         : ihw
 * date           : 2025. 5. 31.
 * description    : 배틀 리스트 조회 vo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BattleListVo {
    private Battle battle;
    private Long leftVote;
    private Long rightVote;
    private String username;
    private String userImg;
}
