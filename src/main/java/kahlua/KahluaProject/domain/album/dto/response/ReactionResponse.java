package kahlua.KahluaProject.domain.album.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.album.entity.EmojiType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReactionResponse {
    @Schema(description = "사진 ID", example = "200")
    private Long photoId;

    @Schema(description = "반응한 이모지 종류", example = "HEART")
    private EmojiType emojiType;

    @Schema(description = "반응한 이모지의 현재 누적 개수", example = "11")
    private Long currentCount;

    @Schema(description = "현재 사용자가 이 이모지를 누른 상태인지 여부 (취소 시 false)", example = "true")
    private Boolean isClicked;

    @Schema(description = "다른 이모지로 변경 시, 기존에 눌렀던 이모지 종류 (없거나 단순 취소 시 null)", example = "SAD")
    private EmojiType previousEmojiType;

    @Schema(description = "다른 이모지로 변경 시, 기존 이모지의 변경 반영 후 최신 개수", example = "4")
    private Long previousCount;
}
