package com.sw.cmc.adapter.in;

import com.sw.cmc.adapter.in.guide.dto.*;
import com.sw.cmc.adapter.in.guide.web.GuideControllerApi;
import com.sw.cmc.application.port.in.GuideUseCase;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in
 * fileName       : GuideControllerImpl
 * author         : ihw
 * date           : 2025. 2. 4.
 * description    : 가이드 api controller
 */
@RestController
@RequiredArgsConstructor
public class GuideControllerImpl implements GuideControllerApi {

    private final GuideUseCase guideUseCase;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<GetGuideQueryResponse> getGuideQuery(Long id) throws Exception {
        Guide guide = guideUseCase.getGuideQuery(id);
        return ResponseEntity.ok(modelMapper.map(guide, GetGuideQueryResponse.class));
    }

    @Override
    public ResponseEntity<GetGuideDTOResponse> getGuideDTO(GuideDTORequest guideDTO) throws Exception {
        Guide guide = guideUseCase.getGuideDTO(guideDTO);
        return ResponseEntity.ok(modelMapper.map(guide, GetGuideDTOResponse.class));
    }

    @Override
    public ResponseEntity<PostGuideDeleteResponse> postGuideDelete(PostGuideDeleteRequest postGuideDeleteRequest) throws Exception {
        PostGuideDeleteResponse postGuideDeleteResponse = new PostGuideDeleteResponse();
        postGuideDeleteResponse.setId(guideUseCase.postGuideDelete(postGuideDeleteRequest));
        return ResponseEntity.ok(postGuideDeleteResponse);
    }

    @Override
    public ResponseEntity<PostGuideCreateResponse> postGuideCreate(PostGuideCreateRequest postGuideCreateRequest) throws Exception {
        Guide savedGuide = guideUseCase.postGuideCreate(postGuideCreateRequest);
        return ResponseEntity.ok(modelMapper.map(savedGuide, PostGuideCreateResponse.class));
    }

    @Override
    public ResponseEntity<PostGuideUpdateResponse> postGuideUpdate(PostGuideUpdateRequest postGuideUpdateRequest) throws Exception {
        Guide guide = guideUseCase.postGuideUpdate(postGuideUpdateRequest);
        return ResponseEntity.ok(modelMapper.map(guide, PostGuideUpdateResponse.class));
    }
}
