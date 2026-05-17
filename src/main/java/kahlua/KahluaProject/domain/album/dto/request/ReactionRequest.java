package kahlua.KahluaProject.domain.album.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.album.entity.EmojiType;
import lombok.Getter;

@Getter
public class ReactionRequest {
    @Schema(description = "이모지 종류 (LAUGH, ANGRY, SAD, HEART, CONFUSED)", example = "HEART")
    private EmojiType emojiType;
}
