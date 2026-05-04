package kahlua.KahluaProject.domain.ticket.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantsCreateRequest {

    @Schema(description = "참석자 이름")
    private String name;

    @Schema(description = "참석자 전화번호")
    private String phone_num;
}
