package kahlua.KahluaProject.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.reservation.entity.ReservationStatus;
import kahlua.KahluaProject.domain.reservation.entity.ReservationType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReservationResponse(
        @Schema(description = "예약 id", example = "1")
        Long reservationId,
        @Schema(description = "예약자 이메일", example = "kahlua@kahlua.com")
        String email,
        @Schema(description = "예약 유형", example = "TEAM")
        ReservationType type,
        @Schema(description = "예약자명", example = "깔루아팀")
        String clubroomUsername,

        @Schema(description = "예약 날짜", example = "2024-01-01")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate reservationDate,
        @Schema(description = "사용 시작 시간", example = "10:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime startTime,
        @Schema(description = "사용 종료 시간", example = "11:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime endTime,

        @Schema(description = "예약 상태", example = "RESERVED")
        ReservationStatus status
) {
}
