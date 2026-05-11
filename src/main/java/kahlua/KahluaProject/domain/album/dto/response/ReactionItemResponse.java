package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReactionItemResponse {
    @Schema(description = "이모지 종류 (LIKE, HEART, SMILE, SAD, ANGRY)", example = "HEART")
    private String emojiType;

    @Schema(description = "해당 이모지 누적 개수", example = "10")
    private int count;

    @Schema(description = "현재 접속한 사용자가 눌렀는지 여부", example = "true")
    private boolean isClicked;
}