package kahlua.KahluaProject.domain.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchResponse {
    @Schema(description = "아이디(번호)", example = "1")
    private Long id;

    @Schema(description = "게시글 제목", example = "2024년 9월 정기공연")
    private String title;

    @Schema(description = "게시글 작성자", example = "관리자")
    private String writer;

    @Schema(description = "게시글 좋아요 수", example = "13")
    private int likes;

    @Schema(description = "게시글 댓글 수", example = "5")
    private int commentsCount;

    @Schema(description = "작성한 날짜", example = "2024-08-01T00:00:00")
    private LocalDateTime createdAt;

    public PostSearchResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.writer = post.getUser() != null ? post.getUser().getName() : null;
        this.createdAt = post.getCreatedAt();
        this.likes = post.getLikes();
        this.commentsCount = post.getComments().size();
    }
}
