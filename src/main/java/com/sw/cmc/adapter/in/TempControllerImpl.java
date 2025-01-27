package com.sw.cmc.adapter.in;

import com.sw.cmc.adapter.in.cmc.dto.CreateCmcInDTO;
import com.sw.cmc.adapter.in.cmc.web.CmcControllerApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.sw.cmc.adapter.in
 * fileName       : TempControllerImpl
 * author         : IHW
 * date           : 2025-01-24
 * description    :
 */
@RestController
public class TempControllerImpl implements CmcControllerApi {
    @Override
    public ResponseEntity<CreateCmcInDTO> getCmcDetail(Long cmcId) throws Exception {
        return CmcControllerApi.super.getCmcDetail(cmcId);
    }
}
