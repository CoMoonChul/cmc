package com.sw.cmc.domain.comment;

import com.sw.cmc.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.sw.cmc.domain.comment
 * fileName       : CommentVo
 * author         : ihw
 * date           : 2025. 4. 9.
 * description    : 댓글 vo
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Comment comment;
    private String userImg;
}
