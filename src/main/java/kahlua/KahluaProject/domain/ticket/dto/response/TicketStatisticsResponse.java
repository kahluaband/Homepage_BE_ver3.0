package kahlua.KahluaProject.domain.ticket.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TicketStatisticsResponse(
        @Schema(description = "티켓 상태별 수")
        TicketStatusResponse ticketStatusCount,
        @Schema(description = "그래프 정보")
        GraphResponse graph,
        @Schema(description = "총 수입", example = "1000000")
        Long totalIncome
) {
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TicketStatusResponse(
            @Schema(description = "총 티켓 수", example = "100")
            Long totalTicketCount,
            @Schema(description = "결제 대기 수", example = "20")
            Long waitCount,
            @Schema(description = "결제 완료 수", example = "20")
            Long finishPaymentCount,
            @Schema(description = "환불 요청 수", example = "20")
            Long cancelRequestCount
    ) {
    }
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record GraphResponse(
            @Schema(description = "일반 티켓 수", example = "80")
            Long generalCount,
            @Schema(description = "일반 티켓 비율", example = "80")
            Long generalPercent,
            @Schema(description = "신입생 티켓 수", example = "20")
            Long freshmanCount,
            @Schema(description = "신입생 티켓 비율", example = "20")
            Long freshmanPercent
    ) {
    }
}
