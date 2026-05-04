package kahlua.KahluaProject.domain.kahluaInfo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.domain.kahluaInfo.dto.request.LeaderInfoRequest;
import kahlua.KahluaProject.domain.kahluaInfo.dto.response.LeaderInfoResponse;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.kahluaInfo.service.KahluaInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/kahlua-info")
@Tag(name = "깔루아 기본정보", description = "담당자 관련 학회 정보 API")
public class KahluaInfoController {
    private final KahluaInfoService kahluaInfoService;

    @GetMapping("/leader")
    @Operation(summary = "담당자 정보 조회 API", description = "담당자 정보 조회")
    public ApiResponse<LeaderInfoResponse> getLeaderInfo() {
        LeaderInfoResponse response = kahluaInfoService.getLeaderInfo();
        return ApiResponse.onSuccess(response);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @PatchMapping("/leader/update")
    @Operation(summary = "담당자 정보 수정 API", description = "담당자 이름, 연락처, 이메일 수정")
    public ApiResponse<LeaderInfoResponse> updateLeaderInfo(@RequestBody LeaderInfoRequest request,@AuthenticationPrincipal AuthDetails authDetails) {
        LeaderInfoResponse response = kahluaInfoService.updateLeaderInfo(request);
        return ApiResponse.onSuccess(response);
    }
}