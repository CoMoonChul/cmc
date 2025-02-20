package com.sw.cmc.application.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.cmc.common.config.GptConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName    : com.sw.cmc.application.service.ai
 * fileName       : AiService
 * author         : ihw
 * date           : 2025. 2. 19.
 * description    : ai service
 */
@Service
@RequiredArgsConstructor
public class AIService {

    private final GptConfig gptConfig;

    public String call() throws Exception{
        return callOpenAI();
    }

    private String generateSystemPrompt() {
        return "당신은 코드 리뷰 전문가입니다. 사용자의 코드에 대해 효율적이고 깔끔한 코드를 작성하는 방법을 알려주세요.\n" +
                "답변의 조건을 전달드리겠습니다. 답변의 형식을 반드시 지켜주세요.\n" +
                "조건1. 저에게 설명과 코드를 제시해 주세요. 다른 카테고리는 필요하지 않습니다.\n" +
                "조건2. !!![설명]: 이라는 텍스트를 답변에 포함하고, 한글로 된 설명을 !!![설명]: 뒤에 포함시켜 주세요.\n" +
                "설명이 끝난 부분에 !!! 텍스트를 포함시켜 주세요." +
                "조건3. 설명 영역에 이어서 !!![코드]: 라는 텍스트를 답변에 포함하고, 코드를 !!![코드]: 뒤에 포함시켜 주세요\n" +
                "코드가 끝난 부분에 !!! 텍스트를 포함시켜 주세요." +
                "답변 형태 예시는 조건에 맞게 !!![설명]:설명내용...!!!!!![코드]코드내용...!!! 형태가 되어야 합니다.";
    }

    private String generateUserPrompt() {
        String tempCode = "package com.sw.cmc.application.service.comment;\n" +
                "\n" +
                "\n" +
                "import com.sw.cmc.adapter.out.comment.persistence.CommentRepository;\n" +
                "import com.sw.cmc.application.port.in.comment.CommentUseCase;\n" +
                "import com.sw.cmc.application.service.ai.AIService;\n" +
                "import com.sw.cmc.common.advice.CmcException;\n" +
                "import com.sw.cmc.common.util.MessageUtil;\n" +
                "import com.sw.cmc.domain.comment.CommentDomain;\n" +
                "import com.sw.cmc.domain.comment.CommentListDomain;\n" +
                "import com.sw.cmc.entity.Comment;\n" +
                "import jakarta.persistence.EntityManager;\n" +
                "import jakarta.persistence.PersistenceContext;\n" +
                "import jakarta.transaction.Transactional;\n" +
                "import lombok.RequiredArgsConstructor;\n" +
                "import org.modelmapper.ModelMapper;\n" +
                "import org.springframework.data.domain.Page;\n" +
                "import org.springframework.data.domain.PageRequest;\n" +
                "import org.springframework.data.domain.Pageable;\n" +
                "import org.springframework.data.domain.Sort;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * packageName    : com.sw.cmc.application.service.comment\n" +
                " * fileName       : CommentService\n" +
                " * author         : ihw\n" +
                " * date           : 2025. 2. 13.\n" +
                " * description    : comment service\n" +
                " */\n" +
                "@Service\n" +
                "@RequiredArgsConstructor\n" +
                "public class CommentService implements CommentUseCase {\n" +
                "\n" +
                "    @PersistenceContext\n" +
                "    private final EntityManager entityManager;\n" +
                "    private final ModelMapper modelMapper;\n" +
                "    private final CommentRepository commentRepository;\n" +
                "    private final MessageUtil messageUtil;\n" +
                "    private final AIService aiService;\n" +
                "\n" +
                "    @Override\n" +
                "    public CommentDomain selectComment(Long id) throws Exception {\n" +
                "        aiService.call();\n" +
                "        Comment found = commentRepository.findById(id)\n" +
                "                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage(\"COMMENT001\")));\n" +
                "        return CommentDomain.builder()\n" +
                "                .commentId(found.getCommentId())\n" +
                "                .content(found.getContent())\n" +
                "                .userNum(found.getUser().getUserNum())\n" +
                "                .targetId(found.getTargetId())\n" +
                "                .commentTarget(found.getCommentTarget())\n" +
                "                .createdAt(found.getCreatedAt())\n" +
                "                .updatedAt(found.getUpdatedAt())\n" +
                "                .build();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public CommentListDomain selectCommentList(Long targetId, Integer commentTarget, Integer page, Integer size) throws Exception {\n" +
                "        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, \"createdAt\"));\n" +
                "        Page<Comment> res = commentRepository.findByTargetIdAndCommentTarget(targetId, commentTarget, pageable);\n" +
                "        List<CommentDomain> list = res.getContent().stream().map(this::convertEntityToDomain).toList();\n" +
                "\n" +
                "        return CommentListDomain.builder()\n" +
                "                .pageNumber(res.getPageable().getPageNumber())\n" +
                "                .pageSize(res.getPageable().getPageSize())\n" +
                "                .totalElements(res.getTotalElements())\n" +
                "                .totalPages(res.getTotalPages())\n" +
                "                .commentList(list)\n" +
                "                .build();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @Transactional\n" +
                "    public CommentDomain createComment(CommentDomain commentDomain) throws Exception {\n" +
                "        commentDomain.validateCreateComment();\n" +
                "        Comment saved = commentRepository.save(modelMapper.map(commentDomain, Comment.class));\n" +
                "        entityManager.refresh(saved);\n" +
                "        return CommentDomain.builder()\n" +
                "                .commentId(saved.getCommentId())\n" +
                "                .content(saved.getContent())\n" +
                "                .userNum(saved.getUser().getUserNum())\n" +
                "                .targetId(saved.getTargetId())\n" +
                "                .commentTarget(saved.getCommentTarget())\n" +
                "                .createdAt(saved.getCreatedAt())\n" +
                "                .updatedAt(saved.getUpdatedAt())\n" +
                "                .build();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @Transactional\n" +
                "    public CommentDomain deleteComment(CommentDomain commentDomain) throws Exception {\n" +
                "        commentDomain.validateDeleteComment();\n" +
                "        commentRepository.deleteById(commentDomain.getCommentId());\n" +
                "        return commentDomain;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    @Transactional\n" +
                "    public CommentDomain updateComment(CommentDomain commentDomain) throws Exception {\n" +
                "        commentDomain.validateUpdateComment();\n" +
                "        Comment found = commentRepository.findById(commentDomain.getCommentId())\n" +
                "                .orElseThrow(() -> new CmcException(messageUtil.getFormattedMessage(\"COMMENT001\")));\n" +
                "        found.setContent(commentDomain.getContent());\n" +
                "        found.setTargetId(commentDomain.getTargetId());\n" +
                "        found.setCommentTarget(commentDomain.getCommentTarget());\n" +
                "        Comment saved = commentRepository.save(found);\n" +
                "        return CommentDomain.builder()\n" +
                "                .commentId(saved.getCommentId())\n" +
                "                .content(saved.getContent())\n" +
                "                .userNum(saved.getUser().getUserNum())\n" +
                "                .targetId(saved.getTargetId())\n" +
                "                .commentTarget(saved.getCommentTarget())\n" +
                "                .createdAt(saved.getCreatedAt())\n" +
                "                .updatedAt(saved.getUpdatedAt())\n" +
                "                .build();\n" +
                "    }\n" +
                "\n" +
                "    private CommentDomain convertEntityToDomain(Comment c) {\n" +
                "        return new CommentDomain(\n" +
                "                c.getCommentId(),\n" +
                "                c.getContent(),\n" +
                "                c.getUser().getUserNum(),\n" +
                "                c.getTargetId(),\n" +
                "                c.getCommentTarget(),\n" +
                "                c.getCreatedAt(),\n" +
                "                c.getUpdatedAt(),\n" +
                "                c.getUser().getUsername(),\n" +
                "                c.getUser().getEmail()\n" +
                "        );\n" +
                "    }\n" +
                "}\n";
        String tempType = "java";
        return "저는 효율적이고 깔끔한 코드를 작성하기 위해 노력하는 개발자입니다. 제시한 조건을 반드시 지켜 코드 리뷰 부탁합니다.\n" +
                String.format("코드: %s, 언어: %s", tempCode, tempType);
    }

    private String callOpenAI() throws JsonProcessingException {
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
                "content", generateUserPrompt()
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
