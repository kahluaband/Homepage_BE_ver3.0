package kahlua.KahluaProject.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.user.dto.request.UserProfileRequest;
import kahlua.KahluaProject.domain.user.dto.response.UserProfileResponse;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.user.converter.UserConverter;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.dto.request.UserInfoRequest;
import kahlua.KahluaProject.domain.user.dto.response.UserResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "사용자", description = "사용자와 관련된 API")
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "사용자 정보 입력", description = "사용자의 정보를 입력")
    public ApiResponse<UserResponse> createUserInfo(@AuthenticationPrincipal AuthDetails authDetails,
                                                    @RequestBody(required = false) UserInfoRequest userInfoRequest) {
        return ApiResponse.onSuccess(userService.createUserInfo(authDetails.user().getId(), userInfoRequest));
    }

    @GetMapping("")
    @Operation(summary = "사용자 정보 조회", description = "사용자의 정보를 조회")
    public ApiResponse<UserResponse> getUser(@AuthenticationPrincipal AuthDetails authDetails) {
        User user = authDetails.user();
        return ApiResponse.onSuccess(UserConverter.toUserResDto(user));
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 이미지 조회", description = "로그인한 사용자의 프로필 이미지를 조회합니다.")
    public ApiResponse<UserProfileResponse> getUserProfileImage(
            @AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.onSuccess(userService.getUserProfileImage(authDetails.user().getId()));
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 이미지 수정", description = "사용자의 프로필 이미지를 수정합니다.")
    public ApiResponse<UserProfileResponse> updateUserProfileImage(@AuthenticationPrincipal AuthDetails authDetails,
                                                                   @RequestBody UserProfileRequest request) {
        return ApiResponse.onSuccess(userService.updateUserProfileImage(authDetails.user().getId(), request));
    }

    @DeleteMapping("/profile")
    @Operation(summary = "프로필 이미지 삭제", description = "프로필 이미지를 삭제(기본 이미지로 초기화)합니다.")
    public ApiResponse<UserProfileResponse> deleteUserProfileImage(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.onSuccess(userService.deleteUserProfileImage(authDetails.user().getId()));
    }
}
