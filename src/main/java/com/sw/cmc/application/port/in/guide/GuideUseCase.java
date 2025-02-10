package com.sw.cmc.application.port.in.guide;

import com.sw.cmc.adapter.in.guide.dto.*;
import com.sw.cmc.domain.guide.GuideDomain;

/**
 * packageName    : com.sw.cmc.application.port.in
 * fileName       : GuideUseCase
 * author         : ihw
 * date           : 2025. 2. 4.
 * description    : 가이드 api usecase
 */
public interface GuideUseCase {
    /**
     * methodName : getGuideQuery
     * author : IM HYUN WOO
     * description : id를 이용해 가이드 조회 (input을 쿼리 파라미터로 받는 get api)
     *
     * @param id
     * @return get guide query response
     */
    Guide getGuideQuery(Long id) throws Exception;

    /**
     * methodName : getGuideDTO
     * author : IM HYUN WOO
     * description : id를 이용해 가이드 조회 (input을 DTO로 받는 get api)
     *
     * @param guide
     * @return get guide dto response
     */
    Guide getGuideDTO(GuideDomain guide) throws Exception;

    /**
     * methodName : postGuideDelete
     * author : IM HYUN WOO
     * description : post 방식을 이용한 가이드 삭제
     *
     * @param post guide delete request
     * @return post guide delete response
     */
    Long postGuideDelete(GuideDomain guide) throws Exception;

    /**
     * methodName : postGuideCreate
     * author : IM HYUN WOO
     * description : post 방식을 이용한 가이드 생성
     *
     * @param post guide create request
     * @return post guide create response
     */
    Guide postGuideCreate(GuideDomain guide) throws Exception;

    /**
     * methodName : postGuideUpdate
     * author : IM HYUN WOO
     * description : post 방식을 이용한 가이드 수정
     *
     * @param post guide update request
     * @return post guide update response
     */
    Guide postGuideUpdate(GuideDomain guide) throws Exception;
}
