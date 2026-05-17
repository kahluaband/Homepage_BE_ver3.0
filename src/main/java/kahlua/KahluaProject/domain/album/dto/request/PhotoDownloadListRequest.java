package kahlua.KahluaProject.domain.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.util.List;

@Getter
public class PhotoDownloadListRequest {
    @Schema(description = "다운로드할 사진 ID 목록", example = "[200, 201, 205, 210]")
    private List<Long> photoIds;
}
