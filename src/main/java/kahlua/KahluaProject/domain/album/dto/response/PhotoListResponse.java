package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PhotoListResponse {

    @Schema(description = "앨범 ID", example = "1")
    private Long albumId;

    @Schema(description = "앨범 제목", example = "깔루아 공유 앨범")
    private String albumTitle;

    @Schema(description = "사진 목록")
    private List<PhotoItemResponse> content;

    @Schema(description = "마지막 사진의 ID (다음 요청 시 cursor로 사용)", example = "189")
    private Long cursor;

    @Schema(description = "다음 데이터를 더 불러올 수 있는지 여부", example = "true")
    private Boolean hasNext;
}
