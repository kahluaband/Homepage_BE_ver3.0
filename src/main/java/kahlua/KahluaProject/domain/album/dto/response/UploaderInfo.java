package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploaderInfo {
    @Schema(description = "업로더 ID", example = "42")
    private Long id;

    @Schema(description = "업로더 이름", example = "이한재")
    private String name;

    @Schema(description = "업로더 기수", example = "21기")
    private String term;
}
