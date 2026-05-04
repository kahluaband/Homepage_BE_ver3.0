package kahlua.KahluaProject.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfileResponse {
    @Schema(description = "id")
    private Long id;

    @Schema(description = "image url")
    private String userProfileImageUrl;
}
