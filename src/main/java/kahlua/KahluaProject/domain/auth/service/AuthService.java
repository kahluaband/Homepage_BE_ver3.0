package kahlua.KahluaProject.domain.auth.service;

import kahlua.KahluaProject.domain.user.service.UserService;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.auth.converter.AuthConverter;
import kahlua.KahluaProject.domain.user.converter.UserConverter;
import kahlua.KahluaProject.domain.user.entity.Credential;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.dto.request.SignInRequest;
import kahlua.KahluaProject.domain.user.dto.request.SignUpRequest;
import kahlua.KahluaProject.domain.user.dto.response.SignInResponse;
import kahlua.KahluaProject.domain.user.dto.response.TokenResponse;
import kahlua.KahluaProject.domain.user.dto.response.UserResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.redis.RedisClient;
import kahlua.KahluaProject.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final CredentialService credentialService;
    private final JwtProvider jwtProvider;
    private final RedisClient redisClient;

    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest) {
        Credential credential = credentialService.createCredential(signUpRequest);
        User user = userService.createUser(credential, signUpRequest);

        return UserConverter.toUserResDto(user);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userService.getUserByEmail(signInRequest.getEmail());
        credentialService.checkPassword(user, signInRequest.getPassword());

        TokenResponse tokenResponse = jwtProvider.createToken(user);
        redisClient.setValue(user.getEmail(), tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7L);

        return AuthConverter.toSignInResDto(user, tokenResponse);
    }

    @Transactional
    public void signOut(String refreshToken, String accessToken) {
        if (!jwtProvider.validateToken(accessToken, "access")) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        jwtProvider.invalidateTokens(refreshToken, accessToken);
    }

    public TokenResponse recreate(String token, User user) {
        if (user == null) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        String refreshToken = token.substring(7);
        boolean isValid = jwtProvider.validateToken(refreshToken, "refresh");

        if (!isValid) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        String email = jwtProvider.getEmail(refreshToken);
        String redisRefreshToken = redisClient.getValue(email);

        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(redisRefreshToken) || !redisRefreshToken.equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        return jwtProvider.recreate(user, refreshToken);
    }

    @Transactional
    public void withdraw(User user, String refreshToken, String accessToken) {
        if (user == null) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        if (!jwtProvider.validateToken(accessToken, "access")) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        userService.withdraw(user);
        jwtProvider.invalidateTokens(refreshToken, accessToken);
    }
}
