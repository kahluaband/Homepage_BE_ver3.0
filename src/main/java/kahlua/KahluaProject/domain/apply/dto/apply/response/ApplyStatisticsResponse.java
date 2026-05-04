package kahlua.KahluaProject.domain.apply.dto.apply.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ApplyStatisticsResponse(
        @Schema(description = "총 지원자 수", example = "100")
        Long totalApplyCount,

        @Schema(description = "보컬 지원자 수", example = "20")
        Long vocalCount,

        @Schema(description = "보컬 지원자 비율", example = "20")
        Long vocalPercent,

        @Schema(description = "드럼 지원자 수", example = "20")
        Long drumCount,

        @Schema(description = "드럼 지원자 비율", example = "20")
        Long drumPercent,

        @Schema(description = "기타 지원자 수", example = "20")
        Long guitarCount,

        @Schema(description = "기타 지원자 비율", example = "20")
        Long guitarPercent,

        @Schema(description = "베이스 지원자 수", example = "20")
        Long bassCount,

        @Schema(description = "베이스 지원자 비율", example = "20")
        Long bassPercent,

        @Schema(description = "신디사이저 지원자 수", example = "20")
        Long synthesizerCount,

        @Schema(description = "신디사이저 지원자 비율", example = "20")
        Long synthesizerPercent
) {
}
