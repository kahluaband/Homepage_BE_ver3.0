package kahlua.KahluaProject.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.post.entity.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {

    @Schema(description = "게시글 제목", example = "2024년 9월 정기공연")
    private String title;

    @Schema(description = "게시글 내용", example = "안녕하세요 깔루아 기장입니다. 감사합니다.")
    private String content;

    @Schema(description = "게시글 사진 리스트", example = "[\n" +
            "    \"https://bucketname.s3.region.amazonaws.com/image1.jpg\"\n" +
            "  ]")
    private List<PostImageCreateRequest> imageUrls;

    @Schema(description = "게시판 구분", example = "NOTICE")
    private PostType postType;
}
