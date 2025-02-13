package com.sw.cmc.domain.edt;

import com.sw.cmc.domain.lcd.LiveCoding;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.sw.cmc.domain
 * fileName       : Editro
 * author         : Ko
 * date           : 2025-02-09
 * description    :
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "editor_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private LiveCoding liveCoding; // 라이브코딩

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String codeType;




    @Builder
    public Editor(LiveCoding liveCoding, String code, String codeType) {
        this.liveCoding = liveCoding;
        this.code = code;
        this.codeType = codeType;
    }}