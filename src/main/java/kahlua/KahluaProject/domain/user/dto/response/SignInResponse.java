package kahlua.KahluaProject.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.user.entity.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInResponse {

    @Schema(description = "아이디")
    private Long id;

    @Schema(description = "이메일(아이디)")
    private String email;

    @Schema(description = "권한")
    private UserType role;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "기수")
    private Long term;

    @Schema(description = "세션")
    private String session;

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;
}
