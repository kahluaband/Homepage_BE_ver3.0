package kahlua.KahluaProject.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import kahlua.KahluaProject.domain.reservation.entity.ReservationType;

import java.time.LocalTime;

public record ReservationRequest(
        ReservationType type,
        String clubroomUsername,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        LocalTime endTime
) {
}
