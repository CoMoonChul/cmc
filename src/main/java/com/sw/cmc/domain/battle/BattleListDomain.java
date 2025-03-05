package com.sw.cmc.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.battle
 * fileName       : BattleListDomain
 * author         : ihw
 * date           : 2025. 3. 5.
 * description    : battle list domain
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleListDomain {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private List<BattleDomain> battleList;
}
