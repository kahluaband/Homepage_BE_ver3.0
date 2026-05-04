package kahlua.KahluaProject.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.user.entity.UserType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    @Schema(description = "아이디")
    private Long id;

    @Schema(description = "이메일(아이디)")
    private String email;

    @Schema(description = "권한")
    private UserType role;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "세션")
    private String session;

    @Schema(description = "기수")
    private Long term;
}
