package kahlua.KahluaProject.domain.user.service;

import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.auth.converter.AuthConverter;
import kahlua.KahluaProject.domain.user.entity.LoginType;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.user.dto.response.SignInResponse;
import kahlua.KahluaProject.domain.user.dto.response.TokenResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.redis.RedisClient;
import kahlua.KahluaProject.domain.user.repository.UserRepository;
import kahlua.KahluaProject.global.security.google.GoogleClient;
import kahlua.KahluaProject.global.security.google.dto.GoogleProfile;
import kahlua.KahluaProject.global.security.google.dto.GoogleToken;
import kahlua.KahluaProject.global.security.jwt.JwtProvider;
import kahlua.KahluaProject.global.security.kakao.KakaoClient;
import kahlua.KahluaProject.global.security.kakao.dto.KakaoProfile;
import kahlua.KahluaProject.global.security.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SocialLoginService {

    @Value("${spring.security.oauth2.client.registration.google-local.redirect-uri}")
    private String googleLocalRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.google-prod.redirect-uri}")
    private String googleProdRedirectUrl;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final KakaoClient kakaoClient;
    private final RedisClient redisClient;
    private final GoogleClient googleClient;

    @Transactional
    public SignInResponse signInWithGoogle(String code, String redirectUri) {

        // 구글로 액세스 토큰 요청하기
        GoogleToken googleAccessToken;
        if(redirectUri.equals(googleLocalRedirectUrl)) {
            googleAccessToken = googleClient.getGoogleAccessToken(code, googleLocalRedirectUrl);
        } else {
            googleAccessToken = googleClient.getGoogleAccessToken(code, googleProdRedirectUrl);
        }

        // 구글에 있는 사용자 정보 반환
        GoogleProfile googleProfile = googleClient.getMemberInfo(googleAccessToken);

        // 반환된 정보의 이메일 기반으로 사용자 테이블에서 계정 정보 조회 진행
        String email = googleProfile.email();
        if (email == null) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        //bussiness logic: 사용자 정보가 이미 있다면 로그인 타입 확인 후 해당 사용자 정보를 반환하고, 없다면 새로운 사용자 정보를 생성하여 반환
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseGet(() -> createUser(email, LoginType.GOOGLE));

        if (user.getLoginType() != LoginType.GOOGLE) {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_USER);
        }

        TokenResponse tokenResponse = jwtProvider.createToken(user);
        redisClient.setValue(user.getEmail(), tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7L);

        return AuthConverter.toSignInResDto(user, tokenResponse);
    }

    @Transactional
    public SignInResponse signInWithKakao(String code, String redirectUrl) {
        // 카카오로 액세스 토큰 요청하기
        KakaoToken kakaoToken = kakaoClient.getAccessTokenFromKakao(code, redirectUrl);

        // 카카오에 있는 사용자 정보 반환
        KakaoProfile kakaoProfile = kakaoClient.getMemberInfo(kakaoToken);

        // 반환된 정보의 이메일 기반으로 사용자 테이블에서 계정 정보 조회 진행
        String email = kakaoProfile.kakao_account().email();
        if (email == null) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        //bussiness logic: 사용자 정보가 이미 있다면 로그인 타입 확인 후 해당 사용자 정보를 반환하고, 없다면 새로운 사용자 정보를 생성하여 반환
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseGet(() -> createUser(email, LoginType.KAKAO));

        if (user.getLoginType() != LoginType.KAKAO) {
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_USER);
        }

        TokenResponse tokenResponse = jwtProvider.createToken(user);
        redisClient.setValue(user.getEmail(), tokenResponse.getRefreshToken(), 1000 * 60 * 60 * 24 * 7L);

        return AuthConverter.toSignInResDto(user, tokenResponse);
    }

    @Transactional
    public User createUser(String email, LoginType loginType) {
        User user = User.builder()
                .email(email)
                .userType(UserType.GENERAL)
                .name(null)
                .term(null)
                .session(null)
                .loginType(loginType)
                .credential(null)
                .build();
        return userRepository.save(user);
    }
}

