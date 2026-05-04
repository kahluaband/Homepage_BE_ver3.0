package kahlua.KahluaProject.domain.performance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.performance.dto.request.PerformanceRequest;
import kahlua.KahluaProject.domain.performance.dto.response.PerformanceListResponse;
import kahlua.KahluaProject.domain.performance.dto.response.PerformanceResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.domain.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공연 페이지", description = "공연페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/performances")
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping
    @Operation(summary = "공연 리스트 조회 API", description = "공연들을 조회하는 API입니다.")
    public ApiResponse<PerformanceListResponse.performanceListDto> getPerformances(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "8") int limit){
        return ApiResponse.onSuccess(performanceService.getPerformances(cursor, limit));
    }

    @GetMapping("/{performance_id}")
    @Operation(summary = "공연 상세정보 조회 API", description = "특정 공연의 상세정보를 조회하는 API입니다.")
    public ApiResponse<PerformanceListResponse.performanceInfoDto> getLatestPerformanceInfo(@PathVariable Long performance_id){
        return ApiResponse.onSuccess(performanceService.getPerformanceInfo(performance_id));
    }

    @GetMapping("/latest-performance")
    @Operation(summary = "최신 공연 상세정보 조회 API", description = "최신 공연의 상세정보를 조회하는 API입니다.")
    public ApiResponse<PerformanceListResponse.performanceInfoDto> getLatestPerformanceInfo(){
        return ApiResponse.onSuccess(performanceService.getLatestPerformanceInfo());
    }

    @CheckUserType(userType = UserType.ADMIN)
    @PostMapping("/create")
    @Operation(summary = "공연정보 생성 API", description = "새로운 공연을 생성하는 API입니다.")
    public ApiResponse<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest ticketCreateRequest, @AuthenticationPrincipal AuthDetails authDetails){
        PerformanceResponse ticketinfoResponse = performanceService.createPerformance(ticketCreateRequest);
        return ApiResponse.onSuccess(ticketinfoResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @DeleteMapping("/{performanceId}")
    @Operation(summary = "공연 삭제 API", description = "공연을 삭제하는 API입니다.")
    public ApiResponse<Void> deletePerformance(@PathVariable Long performanceId,@AuthenticationPrincipal AuthDetails authDetails){
        performanceService.deletePerformance(performanceId);

        return ApiResponse.onSuccess(null);
    }
}

