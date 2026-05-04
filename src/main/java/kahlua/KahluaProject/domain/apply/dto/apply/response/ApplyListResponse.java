package kahlua.KahluaProject.domain.apply.dto.apply.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(title = "지원자 리스트 응답 DTO")
public class ApplyListResponse {

    @Schema(description = "전체 지원자 수")
    private Long total;

    @Schema(description = "지원자 리스트")
    private List<ApplyItemResponse> applies;
}
