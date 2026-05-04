package kahlua.KahluaProject.domain.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kahlua.KahluaProject.global.apipayload.ApiResponse;
import kahlua.KahluaProject.domain.reservation.dto.request.ReservationProceedRequest;
import kahlua.KahluaProject.domain.reservation.dto.request.ReservationRequest;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationListResponse;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationResponse;
import kahlua.KahluaProject.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Tag(name = "동방 예약", description = "동방 예약 기능 관련 API")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 시간 선택한 사람에게 우선권 부여
    @MessageMapping("/reserve.proceed/{date}")
    @SendTo("/topic/public/{date}")
    public ReservationResponse proceed(@DestinationVariable String date,
                                       @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                                       @Payload ReservationProceedRequest reservationProceedRequest) {

        return reservationService.proceed(reservationProceedRequest, date, simpSessionAttributes);
    }

    // 예약 확정 후 예약내역 DB에 저장
    @MessageMapping("/reserve.complete/{date}")
    @SendTo("/topic/public/{date}")
    public ReservationResponse complete(@DestinationVariable String date,
                                       @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes,
                                       @Payload ReservationRequest reservationRequest) {

        return reservationService.save(reservationRequest, date, simpSessionAttributes);
    }

    @GetMapping("/v1/reservation")
    @Operation(summary = "날짜별 예약내역 목록 조회", description = "지정한 날짜에 해당하는 예약내역 목록을 사용 시작 시간 오름차순으로 조회합니다." +
            "<br> 쿼리 파라미터 날짜 형식은 yyyy-MM-dd 입니다")
    public ApiResponse<ReservationListResponse> getReservationListByDate(@RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return ApiResponse.onSuccess(reservationService.getByDate(date));
    }
}
