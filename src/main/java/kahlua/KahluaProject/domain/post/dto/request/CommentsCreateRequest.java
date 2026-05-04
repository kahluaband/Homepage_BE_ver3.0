package kahlua.KahluaProject.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentsCreateRequest {

    @Schema(description = "댓글 작성자", example = "깔루아 홍길동")
    private String user;

    @Schema(description = "댓글 내용", example = "감사합니다.")
    private String content;

    @Schema(description = "대댓글인 경우 부모 댓글 id", example = "네 수고하세요.")
    private Long parentCommentId;
}
