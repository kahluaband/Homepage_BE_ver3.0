package kahlua.KahluaProject.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.user.dto.request.SignInRequest;
import kahlua.KahluaProject.domain.user.dto.request.SignUpRequest;
import kahlua.KahluaProject.domain.user.dto.response.SignInResponse;
import kahlua.KahluaProject.domain.user.dto.response.TokenResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.global.security.jwt.JwtProvider;
import kahlua.KahluaProject.domain.auth.service.AuthService;
import kahlua.KahluaProject.domain.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "인증", description = "사용자 회원가입/로그인/로그아웃/토큰재발급 등 인증과 관련된 API")
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SocialLoginService socialLoginService;
    private final JwtProvider jwtProvider;

    @PostMapping("/google/sign-in")
    @Operation(summary = "구글 로그인", description = "구글 로그인을 통한 회원가입 및 로그인")
    public ResponseEntity<SignInResponse> signInWithGoogle(@RequestParam("code") String code, @RequestParam("redirectUri") String redirectUri) {
        return ResponseEntity.ok(socialLoginService.signInWithGoogle(code, redirectUri));
    }

    @PostMapping("/kakao/sign-in")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 통한 회원가입 및 로그인")
    public ResponseEntity<SignInResponse> signInWithKakao(@RequestParam("code") String code, @RequestParam("redirectUri") String redirectUri) {
        return ResponseEntity.ok(socialLoginService.signInWithKakao(code, redirectUri));
    }

    @PostMapping("/sign-up")
    @Operation(summary = "일반 회원가입", description = "사용자의 정보 입력 후 회원가입")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity
                .created(URI.create("v1/auth/sign-up"))
                .body(ApiResponse.onSuccess(authService.signUp(signUpRequest)));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "일반 로그인", description = "로그인 기능")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity
                .ok()
                .body(ApiResponse.onSuccess(authService.signIn(signInRequest)));
    }

    @PostMapping("/sign-out")
    @Operation(summary = "일반/카카오/구글 로그아웃", description = "로그아웃 기능", security = @SecurityRequirement(name = "JWT Authentication"))
    public ResponseEntity<ApiResponse<String>> signOut(HttpServletRequest request) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        String accessToken = jwtProvider.resolveAccessToken(request);

        if(refreshToken==null || accessToken==null){
            throw new GeneralException(ErrorStatus.TOKEN_NOT_FOUND);
        }

        authService.signOut(refreshToken, accessToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃이 성공적으로 처리되었습니다."));
    }

    @Operation(summary = "토큰재발급", description = "RefreshToken 정보로 요청", security = @SecurityRequirement(name = "JWT Authentication"))
    @GetMapping("/recreate")
    public ResponseEntity<TokenResponse> recreate(HttpServletRequest request, @AuthenticationPrincipal AuthDetails authDetails) {
        String token = request.getHeader("Authorization");
        if(token ==null){
            throw new GeneralException(ErrorStatus.TOKEN_NOT_FOUND);
        }
        return ResponseEntity.ok().body(authService.recreate(token, authDetails.user()));
    }

    @Operation(summary = "일반/카카오/구글 회원탈퇴", description = "회원탈퇴 기능", security = @SecurityRequirement(name = "JWT Authentication"))
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(HttpServletRequest request, @AuthenticationPrincipal AuthDetails authDetails) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        String accessToken = jwtProvider.resolveAccessToken(request);

        if(refreshToken==null || accessToken==null){
            throw new GeneralException(ErrorStatus.TOKEN_NOT_FOUND);
        }

        authService.withdraw(authDetails.user(), refreshToken, accessToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("회원탈퇴가 성공적으로 처리되었습니다."));
    }
}
