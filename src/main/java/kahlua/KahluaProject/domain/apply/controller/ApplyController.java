package kahlua.KahluaProject.domain.apply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.apply.dto.apply.request.ApplyCreateRequest;
import kahlua.KahluaProject.domain.apply.dto.apply.response.ApplyCreateResponse;
import kahlua.KahluaProject.domain.apply.dto.apply.response.ApplyGetResponse;
import kahlua.KahluaProject.domain.apply.service.ApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지원하기", description = "지원하기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apply")
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    @Operation(summary = "지원자 생성", description = "지원서 양식에 맞추어 지원자를 생성합니다")
    public ApiResponse<ApplyCreateResponse> createApplicant(@RequestBody ApplyCreateRequest applyCreateRequest) {
        return ApiResponse.onSuccess(applyService.createApply(applyCreateRequest));
    }

    @GetMapping("/{applyId}")
    @Operation(summary = "지원자 조회", description = "지원 폼 아이디를 통해 지원자를 조회합니다")
    public ApiResponse<ApplyGetResponse> getApplicant(@PathVariable Long applyId) {
        return ApiResponse.onSuccess(applyService.getApply(applyId));
    }
}
