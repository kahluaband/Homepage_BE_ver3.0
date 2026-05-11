package kahlua.KahluaProject.domain.album.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PhotoDetailResponse {
    @Schema(description = "사진 ID", example = "200")
    private Long photoId;

    @Schema(description = "오리지널(고화질) URL", example = "https://cdn.kahluaband.com/photo200.webp")
    private String originalUrl;

    @Schema(description = "카테고리", example = "공연")
    private String category;

    @Schema(description = "업로더 이름", example = "최승원")
    private UploaderInfo uploader;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private List<ReactionItemResponse> reactions;
}
