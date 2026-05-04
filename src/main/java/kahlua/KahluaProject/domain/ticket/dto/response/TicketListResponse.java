package kahlua.KahluaProject.domain.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(title = "티켓 리스트 응답 DTO")
public class TicketListResponse {

    @Schema(description = "전체 티켓 매수")
    private Integer total;

    @Schema(description = "티켓 리스트")
    private List<TicketItemResponse> tickets;
}
