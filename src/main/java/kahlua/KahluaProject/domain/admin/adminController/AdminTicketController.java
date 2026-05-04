package kahlua.KahluaProject.domain.admin.adminController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.domain.user.entity.UserType;
import kahlua.KahluaProject.global.aop.checkAdmin.CheckUserType;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.ticket.dto.response.TicketListResponse;
import kahlua.KahluaProject.domain.ticket.dto.response.TicketStatisticsResponse;
import kahlua.KahluaProject.domain.ticket.dto.response.TicketUpdateResponse;
import kahlua.KahluaProject.domain.performance.dto.request.PerformanceRequest;
import kahlua.KahluaProject.domain.performance.dto.response.PerformanceResponse;
import kahlua.KahluaProject.global.security.AuthDetails;
import kahlua.KahluaProject.global.service.ExcelConvertService;
import kahlua.KahluaProject.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Tag(name = "관리자(예매하기)", description = "관리자(예매하기) 페이지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/tickets")
public class AdminTicketController {

    private final TicketService ticketService;
    private final ExcelConvertService excelConvertService;

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping
    @Operation(summary = "전체 티켓 리스트 조회", description = "sort-by에 정의된 티켓 속성 기준으로 정렬된 전체 티켓 리스트를 조회합니다 " +
            "</br> sort-by를 쿼리에 포함시키지 않은 경우 (환불 요청 -> 결제 대기 -> 결제 왼료 -> 환불 완료) 순서로, 같은 결제 상태에 대해서는 최신순으로 정렬합니다")
    public ApiResponse<TicketListResponse> getTicketList(@AuthenticationPrincipal AuthDetails authDetails,
                                                         @RequestParam(name = "sort-by", required = false) String sortBy) {
        TicketListResponse ticketListResponse = ticketService.getTicketList(sortBy);
        return ApiResponse.onSuccess(ticketListResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping("/general")
    @Operation(summary = "일반 티켓 리스트 조회", description = "sort-by에 정의된 티켓 속성 기준으로 정렬된 일반 티켓 리스트를 조회합니다 " +
            "</br> sort-by를 쿼리에 포함시키지 않은 경우 (환불 요청 -> 결제 대기 -> 결제 왼료 -> 환불 완료) 순서로, 같은 결제 상태에 대해서는 최신순으로 정렬합니다")
    public ApiResponse<TicketListResponse> getGeneralTicketList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                @RequestParam(name = "sort-by", required = false) String sortBy) {
        TicketListResponse ticketListResponse = ticketService.getGeneralTicketList(sortBy);
        return ApiResponse.onSuccess(ticketListResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping("/freshman")
    @Operation(summary = "신입생 티켓 리스트 조회", description = "sort-by에 정의된 티켓 속성 기준으로 정렬된 신입생 티켓 리스트를 조회합니다 " +
            "</br> sort-by를 쿼리에 포함시키지 않은 경우 (환불 요청 -> 결제 대기 -> 결제 왼료 -> 환불 완료) 순서로, 같은 결제 상태에 대해서는 최신순으로 정렬합니다")
    public ApiResponse<TicketListResponse> getFreshmanTicketList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                 @RequestParam(name = "sort-by", required = false) String sortBy) {
        TicketListResponse ticketListResponse = ticketService.getFreshmanTicketList(sortBy);
        return ApiResponse.onSuccess(ticketListResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @PatchMapping("/{ticketId}/ticket-complete")
    @Operation(summary = "티켓 결제 완료", description = "티켓 결제를 완료한 뒤 어드민 페이지에서 결제 완료를 클릭하는 경우 결제 완료로 상태가 변합니다." +
            "</br> 추가적으로 티켓 구매자에게 결제 완료 확인 메일을 발송합니다.")
    public ApiResponse<TicketUpdateResponse> completePaymentForm(@PathVariable(name = "ticketId") Long ticketId, @AuthenticationPrincipal AuthDetails authDetails) {
        TicketUpdateResponse ticketUpdateResponse = ticketService.completePayment(ticketId);
        return ApiResponse.onSuccess(ticketUpdateResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @PatchMapping("/{ticketId}/cancel-complete")
    @Operation(summary = "티켓 취소 완료", description = "티켓 환불을 완료한 뒤 어드민 페이지에서 취소 완료를 클릭하는 경우 취소가 완료로 상태가 변합니다.")
    public ApiResponse<TicketUpdateResponse> completeCancelForm(@PathVariable(name = "ticketId") Long ticketId, @AuthenticationPrincipal AuthDetails authDetails) {
        TicketUpdateResponse ticketUpdateResponse = ticketService.completeCancelTicket(ticketId);
        return ApiResponse.onSuccess(ticketUpdateResponse);
    }

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping("/download")
    @Operation(summary = "티켓 리스트 엑셀 변환", description = "전체 티켓 리스트를 엑셀 파일로 변환하여 다운로드합니다.")
    public ResponseEntity<InputStreamResource> applyListToExcel(@AuthenticationPrincipal AuthDetails authDetails) throws IOException {
        ByteArrayInputStream in = excelConvertService.ticketListToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tickets.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping("/participants/download")
    @Operation(summary = "참석자 리스트 엑셀 변환", description = "전체 참석자 리스트를 엑셀 파일로 변환하여 다운로드합니다.")
    public ResponseEntity<InputStreamResource> participantsListToExcel(@AuthenticationPrincipal AuthDetails authDetails) throws IOException {
        ByteArrayInputStream in = excelConvertService.participantListToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=participants.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }

    @CheckUserType(userType = UserType.ADMIN)
    @PutMapping("/{performance_id}")
    @Operation(summary = "공연 정보 수정", description = "공연 정보를 수정합니다")
    public ApiResponse<PerformanceResponse> updatePerformance(@PathVariable("performance_id") Long performanceId, @RequestBody PerformanceRequest performanceRequest, @AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.onSuccess(ticketService.updatePerformance(performanceId, performanceRequest));
    }

    @GetMapping("/{performance_id}")
    @Operation(summary = "티켓 정보 조회", description = "티켓 정보를 조회합니다")
    public ApiResponse<PerformanceResponse> getTicketInfo(@PathVariable("performance_id") Long performanceId) {
        return ApiResponse.onSuccess(ticketService.getTicketInfo(performanceId));
    }

    @CheckUserType(userType = UserType.ADMIN)
    @GetMapping("/statistics")
    @Operation(summary = "티켓 통계 조회", description = "티켓 통계를 조회합니다")
    public ApiResponse<TicketStatisticsResponse> getTicketStatistics(@AuthenticationPrincipal AuthDetails authDetails) {
        return ApiResponse.onSuccess(ticketService.getTicketStatistics());
    }
}