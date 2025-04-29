package com.sw.cmc.application.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.adapter.out.ai.persistence.AIRepository;
import com.sw.cmc.application.port.in.ai.AIUseCase;
import com.sw.cmc.common.advice.CmcException;
import com.sw.cmc.common.config.GptConfig;
import com.sw.cmc.common.util.AIUtil;
import com.sw.cmc.common.util.GzipUtil;
import com.sw.cmc.domain.ai.AIDomain;
import com.sw.cmc.entity.AIComment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.application.service.ai
 * fileName       : AiService
 * author         : ihw
 * date           : 2025. 2. 19.
 * description    : AIService
 */
@Service
@RequiredArgsConstructor
public class AIService implements AIUseCase {

    private final GptConfig gptConfig;
    private final AIRepository aiRepository;

    @Override
    public AIDomain selectAIComment(Long id) {
        AIComment found = aiRepository.findByTargetId(id).orElseThrow(() -> new CmcException("AI001"));
        AIDomain.AIDomainBuilder builder = AIDomain.builder()
                .content(found.getContent())
                .codeContent(found.getCodeContent())
                .codeType(found.getCodeType());
        return builder.build();
    }

    @Override
    @Transactional
    public AIDomain createComment(AIDomain aiDomain) throws IOException {
        String decompressed = GzipUtil.decompressGzip(aiDomain.getCodeContent());
        String result = callOpenAI(decompressed, aiDomain.getCodeType());
        String code = AIUtil.extractCode(result);
        String content = AIUtil.extractContent(result);

        if (StringUtils.isBlank(code) ||  "400".equals(code) || "400".equals(content)) {
            return null;
        }

        AIComment saving = new AIComment();
        saving.setContent(content);
        saving.setTargetId(aiDomain.getReviewId());
        saving.setCodeContent(GzipUtil.compressGzip(code));
        saving.setCodeType(aiDomain.getCodeType());

        AIComment saved = aiRepository.save(saving);
        return AIDomain.builder()
                .build();
    }

    @Override
    @Transactional
    public AIDomain deleteComment(AIDomain aiDomain) {
        aiRepository.deleteById(aiDomain.getReviewId());
        return aiDomain;
    }

    private String generateSystemPrompt() {
        return "당신은 코드 리뷰 전문가입니다. 사용자의 코드에 대해 효율적이고 깔끔한 코드를 작성하는 방법을 알려주세요.\n" +
                "답변의 조건을 전달드리겠습니다. 답변의 형식을 반드시 지켜주세요.\n" +
                "조건1. 저에게 설명과 코드를 제시해 주세요. 다른 카테고리는 필요하지 않습니다.\n" +
                "조건2. !!![설명]: 이라는 텍스트를 답변에 포함하고, 한글로 된 설명을 !!![설명]: 뒤에 포함시켜 주세요.\n" +
                "설명이 끝난 부분에 !!! 텍스트를 포함시켜 주세요.\n" +
                "조건3. 설명 영역에 이어서 !!![코드]: 라는 텍스트를 답변에 포함하고, 코드를 !!![코드]: 뒤에 포함시켜 주세요\n" +
                "코드가 끝난 부분에 !!! 텍스트를 포함시켜 주세요.\n" +
                "조건4. 주어진 코드의 의미가 불명확하거나, 어떤 기능을 수행하는지 이해하기 어려운 경우,\n" +
                "코드 내용에 400이라는 문자만 넣어주세요.\n" +
                "답변 형태 예시는 조건에 맞게 !!![설명]:설명내용...!!!!!![코드]코드내용...!!! 형태가 되어야 합니다.";
    }

    private String generateUserPrompt(String codeContent, String codeType) {
        return "저는 효율적이고 깔끔한 코드를 작성하기 위해 노력하는 개발자입니다. 제시한 조건을 반드시 지켜 코드 리뷰 부탁합니다.\n" +
                String.format("코드: %s, 언어: %s", codeContent, codeType);
    }

    private String callOpenAI(String codeContent, String codeType) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptConfig.getSecretKey());
        String model = gptConfig.getModel();

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", generateSystemPrompt()
        ));
        messages.add(Map.of(
                "role", "user",
                "content", generateUserPrompt(codeContent, codeType)
        ));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", 3000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            RestTemplate restTemplate = gptConfig.restTemplate();
            ResponseEntity<String> response = restTemplate.exchange(gptConfig.getPromtUrl(), HttpMethod.POST, entity, String.class);
            return parseOpenAIResponse(response.getBody());
        } catch(Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String parseOpenAIResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            if (jsonResponse.has("choices") && jsonResponse.get("choices").isArray() && jsonResponse.get("choices").size() > 0) {
                JsonNode firstChoice = jsonResponse.get("choices").get(0);
                if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                    return firstChoice.get("message").get("content").asText();
                }
            }
            return "OpenAI 응답이 올바르지 않습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing OpenAI response: " + e.getMessage();
        }
    }
}
