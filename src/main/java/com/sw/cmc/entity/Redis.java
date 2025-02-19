package com.sw.cmc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.sw.cmc.entity
 * fileName       : Redis
 * author         : 82104
 * date           : 2025-02-19
 * description    : redis Entity
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Redis {


    @Id
    private String key;

    private String value;

}
