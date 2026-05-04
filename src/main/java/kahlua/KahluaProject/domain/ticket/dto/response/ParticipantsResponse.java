package kahlua.KahluaProject.domain.ticket.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ParticipantsResponse {

    private Long id;

    @Schema(description = "참석자 이름")
    private String name;

    @Schema(description = "참석자 전화번호")
    private String phone_num;
}
