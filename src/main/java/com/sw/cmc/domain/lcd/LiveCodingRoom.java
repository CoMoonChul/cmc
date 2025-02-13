package com.sw.cmc.domain.lcd;

import com.sw.cmc.domain.Editor;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCoding
 * author         : Ko
 * date           : 2025-02-08
 * description    :
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LiveCodingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;


    @OneToMany(mappedBy = "liveCodingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiveChat> liveChats = new ArrayList<>();

    @Column(nullable = false)
    private int maxParticipants;

    @Builder
    public LiveCodingRoom(String roomName, int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }


    @OneToOne(mappedBy = "liveCodingRoom", cascade = CascadeType.ALL)
    private Editor editor;

//    public void setEditor(Editor editor) {
//        this.editor = editor;
//    }
//
//    // 추가적인 비즈니스 로직을 위한 메서드들
//    public void addLiveChat(LiveChat liveChat) {
//        this.liveChats.add(liveChat);
//        liveChat.setLiveCodingRoom(this);
//    }
//
//    public void removeLiveChat(LiveChat liveChat) {
//        this.liveChats.remove(liveChat);
//        liveChat.setLiveCodingRoom(null);
//    }
}
