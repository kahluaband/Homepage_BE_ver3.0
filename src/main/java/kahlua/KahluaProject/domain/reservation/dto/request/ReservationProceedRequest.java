package kahlua.KahluaProject.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ReservationProceedRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime endTime
) {
}
