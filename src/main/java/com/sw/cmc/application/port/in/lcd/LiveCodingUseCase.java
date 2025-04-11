package com.sw.cmc.application.port.in.lcd;

import com.sw.cmc.adapter.in.livecoding.dto.UpdateLiveCodingSnippetReqDTO;
import com.sw.cmc.adapter.in.livecoding.dto.UpdateLiveCodingSnippetResDTO;
import com.sw.cmc.domain.lcd.LiveCodeSnippetDomain;
import com.sw.cmc.domain.lcd.LiveCodingDomain;

import java.util.UUID;

/**
 * packageName    : com.sw.cmc.application.port.in.lcd
 * fileName       : LiveCodingUseCase
 * author         : Ko
 * date           : 2025-02-16
 * description    : LiveCoding useCase
 */
public interface LiveCodingUseCase {


    /**
     * methodName : createLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 생성
     *
     * @param hostId - 호스트 id
     * @return live coding domain
     */
    // 라이브 코딩 방 생성
    LiveCodingDomain createLiveCoding(Long hostId) throws Exception;

    /**
     * methodName : deleteLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 삭제
     *
     * @param roomId - 방 id
     * @return boolean 성공 true 실패 false
     */
    boolean deleteLiveCoding(UUID roomId) throws Exception;  // 방 삭제 (상태 메시지 반환)

    /**
     * methodName : generateInviteLink
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 초대링크 생성
     *
     * @param roomId - 방 id
     * @return String - 초대링크
     */
    String generateInviteLink(UUID roomId) throws Exception;


    /**
     * methodName : selectLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩 방 조회
     *
     * @param roomId - 방 id
     * @return live coding domain
     * @throws Exception the exception
     */
    LiveCodingDomain selectLiveCoding(UUID roomId) throws Exception;

    /**
     * methodName : updateLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩 방 업데이트
     *
     * @param roomId - 방 id
     * @param userNum - 유저 id
     * @param action - JOIN(0) / LEAVE(1)
     * @return live coding domain
     * @throws Exception the exception
     */
    LiveCodingDomain updateLiveCoding(UUID roomId, Long userNum, int action) throws Exception;

    /**
     * methodName : existsByHostId
     * author : KO YOUNG SUNG
     * description : hostId로 방이 존재하는지 확인
     * @param hostId - 호스트 id
     * @return boolean
     */
    boolean existsByHostId(Long hostId);

    /**
     * methodName : findByRoomId
     * author : KO YOUNG SUNG
     * description : 라브코딩방 조회 (room Id)
     *
     * @param roomId - 방 id
     * @return live coding domain
     */
    LiveCodingDomain findByRoomId(UUID roomId);

    /**
     * methodName : saveLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 저장 (레디스)
     *
     * @param liveCodingDomain - liveCodingDomain
     */
    void saveLiveCoding(LiveCodingDomain liveCodingDomain);


    /**
     * methodName : verifyLiveCoding
     * author : KO YOUNG SUNG
     * description : 라이브코딩방 토큰 검증
     *
     * @param token - jwt 토큰
     */
    LiveCodingDomain verifyLiveCoding(String token) throws Exception;


    /**
     * methodName : selectLiveCodingSnippet
     * author : KO YOUNG SUNG
     * description : 라이브코드 조회
     * @param hostId - 호스트 id

     */
    LiveCodeSnippetDomain selectLiveCodingSnippet(Long hostId) throws Exception;

    /**
     * methodName : updateLiveCodingSnippet
     * author : KO YOUNG SUNG
     * description : 라이브코드 업데이트
     * @param reqDTO - UpdateLiveCodingSnippetReqDTO
     */
    UpdateLiveCodingSnippetResDTO updateLiveCodingSnippet(UpdateLiveCodingSnippetReqDTO reqDTO) throws Exception;
}

