package com.sw.cmc.domain.lcd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.live_coding.model
 * fileName       : LiveCoding
 * author         : Ko
 * date           : 2025-04-03
 * description    : 라이브 코딩챗 도메인 객체
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveCodingChatDomain {
    private int liveCodingChatType; // 채팅 or 입퇴장
    private int action; // 참가 / 나가기
    private String msg;
    private Long usernum;
    private String username;
    private String diff;
    private Object cursorPos;
    private String language;

}