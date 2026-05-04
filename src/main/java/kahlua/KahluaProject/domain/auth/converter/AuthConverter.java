package kahlua.KahluaProject.domain.auth.converter;

import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.dto.response.SignInResponse;
import kahlua.KahluaProject.domain.user.dto.response.TokenResponse;

public class AuthConverter {
    public static SignInResponse toSignInResDto(User user, TokenResponse tokenResponse) {
        return SignInResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .term(user.getTerm())
                .session(String.valueOf(user.getSession()))
                .role(user.getUserType())
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .build();
    }
}
