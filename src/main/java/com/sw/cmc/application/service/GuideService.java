package com.sw.cmc.application.service;

import com.sw.cmc.adapter.in.guide.dto.*;
import com.sw.cmc.adapter.out.persistence.GuideRepository;
import com.sw.cmc.application.port.in.GuideUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.sw.cmc.application.service
 * fileName       : GuideService
 * author         : ihw
 * date           : 2025. 2. 4.
 * description    : 가이드 api 서비스 코드
 */
@Service
@RequiredArgsConstructor
public class GuideService implements GuideUseCase {

    private final GuideRepository guideRepository;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;

    @Override
    public Guide getGuideQuery(Long id) throws Exception{
        return guideRepository.findById(id)
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("GUIDE001")));
    }

    @Override
    public Guide getGuideDTO(GuideDTORequest guideDTO) throws Exception{
        return guideRepository.findById(guideDTO.getId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("GUIDE001")));
    }

    @Override
    public Long postGuideDelete(PostGuideDeleteRequest postGuideDeleteRequest) throws Exception {
        guideRepository.deleteById(postGuideDeleteRequest.getId());
        return postGuideDeleteRequest.getId();
    }

    @Override
    public Guide postGuideCreate(PostGuideCreateRequest postGuideCreateRequest) throws Exception{
        validatePostGuideCreate(postGuideCreateRequest);
        Guide guide = modelMapper.map(postGuideCreateRequest, Guide.class);
        return guideRepository.save(guide);
    }

    @Override
    public Guide postGuideUpdate(PostGuideUpdateRequest postGuideUpdateRequest) throws Exception {
        validatePostGuideUpdate(postGuideUpdateRequest);
        Guide existingGuide = guideRepository.findById(postGuideUpdateRequest.getId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("GUIDE001")));
        existingGuide.setDescription(postGuideUpdateRequest.getDescription());
        return guideRepository.save(existingGuide);
    }

    private void validatePostGuideCreate(PostGuideCreateRequest postGuideCreateRequest) throws Exception {
        validateDescriptionLength(postGuideCreateRequest.getDescription());
    }

    private void validatePostGuideUpdate(PostGuideUpdateRequest postGuideUpdateRequest) throws Exception {
        validateDescriptionLength(postGuideUpdateRequest.getDescription());
    }

    private void validateDescriptionLength(String description) throws Exception {
        if (StringUtils.isBlank(description) || StringUtils.length(description) > 12) {
            throw new CmcException(messageUtil.getFormattedMessage("GUIDE002"));
        }
    }
}
