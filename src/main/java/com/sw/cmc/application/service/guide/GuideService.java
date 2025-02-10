package com.sw.cmc.application.service.guide;

import com.sw.cmc.adapter.in.guide.dto.*;
import com.sw.cmc.adapter.out.persistence.GuideRepository;
import com.sw.cmc.application.port.in.guide.GuideUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.util.MessageUtil;
import com.sw.cmc.domain.guide.GuideDomain;
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
    public Guide getGuideDTO(GuideDomain guideDomain) throws Exception{
        return guideRepository.findById(guideDomain.getId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("GUIDE001")));
    }

    @Override
    public Long postGuideDelete(GuideDomain guideDomain) throws Exception {
        guideRepository.deleteById(guideDomain.getId());
        return guideDomain.getId();
    }

    @Override
    public Guide postGuideCreate(GuideDomain guideDomain) throws Exception{
        validatePostGuideCreate(guideDomain);
        Guide guide = modelMapper.map(guideDomain, Guide.class);
        return guideRepository.save(guide);
    }

    @Override
    public Guide postGuideUpdate(GuideDomain guideDomain) throws Exception {
        validatePostGuideUpdate(guideDomain);
        Guide existingGuide = guideRepository.findById(guideDomain.getId())
                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage("GUIDE001")));
        existingGuide.setDescription(guideDomain.getDescription());
        return guideRepository.save(existingGuide);
    }

    private void validatePostGuideCreate(GuideDomain guideDomain) throws Exception {
        validateDescriptionLength(guideDomain.getDescription());
    }

    private void validatePostGuideUpdate(GuideDomain guideDomain) throws Exception {
        validateDescriptionLength(guideDomain.getDescription());
    }

    private void validateDescriptionLength(String description) throws Exception {
        if (StringUtils.isBlank(description) || StringUtils.length(description) > 12) {
            throw new CmcException(messageUtil.getFormattedMessage("GUIDE002"));
        }
    }
}
