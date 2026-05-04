package kahlua.KahluaProject.domain.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.ticket.entity.Meeting;
import kahlua.KahluaProject.domain.ticket.entity.Participants;
import kahlua.KahluaProject.domain.ticket.entity.Status;
import kahlua.KahluaProject.domain.ticket.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class TicketUpdateResponse {

    @Schema(description = "아이디(번호)")
    private Long id;

    @Schema(description = "구매자 이름")
    private String buyer;

    @Schema(description = "구매자 전화번호")
    private String phone_num;

    @Schema(description = "예약번호")
    private String reservationId;

    @Schema(description = "티켓 종류")
    private Type type;

    @Schema(description = "전공")
    private String major;

    @Schema(description = "학번")
    private String studentId;

    @Schema(description = "뒷풀이 참석 여부")
    private Meeting meeting;

    @Schema(description = "참석자 정보")
    private List<Participants> members;

    @Schema(description = "티켓 상태")
    private Status status;
}
