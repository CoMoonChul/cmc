package com.sw.cmc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : BattleView
 * author         : ihw
 * date           : 2025. 2. 17.
 * description    : battle view entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class BattleView {

    @Id
    private Long battleId;

    private Long viewCount;
}
