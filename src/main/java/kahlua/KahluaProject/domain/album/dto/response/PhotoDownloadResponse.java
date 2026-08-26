package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoDownloadResponse {

    @Schema(description = "사진 ID", example = "200")
    private Long photoId;

    @Schema(description = "사용자가 보게 될 파일 이름", example = "KAHLUA_21기_최승원_20260316.jpg")
    private String fileName;

    @Schema(description = "다운로드를 위한 Presigned URL")
    private String downloadUrl;
}
