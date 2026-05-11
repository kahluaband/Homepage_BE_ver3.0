package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class PresignedUrlResponse {

    @Schema(description = "발급된 URL 목록")
    private List<UrlItem> urlList;

    @Getter
    @Builder
    public static class UrlItem {
        @Schema(description = "S3 직접 업로드를 위한 Presigned-URL")
        private String presignedUrl;

        @Schema(description = "업로드 완료 후 DB에 저장될 최종 CDN 이미지 URL")
        private String imageUrl;
    }
}
