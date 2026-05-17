package kahlua.KahluaProject.domain.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.album.entity.Category;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PhotoRegisterRequest {

    @Schema(description = "등록할 사진 목록 (최대 20개)")
    private List<PhotoItem> photos;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PhotoItem {
        @Schema(description = "원본 이미지 URL")
        private String imageUrl;

        @Schema(description = "s3Key")
        private String s3Key;

        @Schema(description = "카테고리", example = "PERFORMANCE")
        private Category category;

        @Schema(description = "업로더 이름 (참고용)", example = "이한재")
        private String uploader;
    }
}
