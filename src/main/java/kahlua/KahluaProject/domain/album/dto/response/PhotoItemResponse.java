package kahlua.KahluaProject.domain.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PhotoItemResponse {

    @Schema(description = "사진 ID", example = "200")
    private Long photoId;

    @Schema(description = "썸네일(저화질) URL", example = "https://cdn.kahluaband.com/thumb/photo200.webp")
    private String thumbnailUrl;

    @Schema(description = "업로더 이름", example = "최승원")
    private String uploaderName;

    @Schema(description = "카테고리", example = "공연")
    private String category;

    @Schema(description = "생성 일자", example = "2026-03-16")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
