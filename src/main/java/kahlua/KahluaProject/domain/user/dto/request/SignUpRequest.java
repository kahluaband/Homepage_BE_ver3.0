package kahlua.KahluaProject.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import kahlua.KahluaProject.domain.user.entity.Credential;
import kahlua.KahluaProject.domain.user.entity.LoginType;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Schema(title = "회원가입 요청 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotEmpty
    @Schema(description = "사용자 이메일", example = "kahlua123@kahlua.com")
    private String email;

    @NotEmpty
    @Schema(description = "사용자 비밀번호", example = "kahlua123")
    private String password;

    @NotEmpty
    @Schema(description = "사용자 비밀번호 확인", example = "kahlua123")
    private String passwordCheck;

    @NotEmpty
    @Schema(description = "사용자 권한", example = "GENERAL")
    private String userType;

    @Schema(description = "이름", example = "김깔룽")
    String name;

    @Schema(description = "기수", example = "21")
    Long term;

    @Schema( description= "세션", example = "VOCAL | BASS | GUITAR | DRUM | SYNTHESIZER | MANAGER")
    String session;

    public User toUser(Credential credential) {
        return User.builder()
                .credential(credential)
                .email(this.email)
                .userType(UserType.GENERAL)
                .loginType(LoginType.GENERAL)
                .name(null)
                .term(null)
                .session(null)
                .build();
    }
}
