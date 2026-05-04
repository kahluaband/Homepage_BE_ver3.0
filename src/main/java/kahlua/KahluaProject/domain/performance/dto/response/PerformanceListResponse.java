package kahlua.KahluaProject.domain.performance.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.performance.entity.PerformanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


public class PerformanceListResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class performanceListDto{
        private List<performanceDto> performances;
        @Schema(description = "다음 커서 값 (커서는 공연 id임)")
        private Long nextcursor;
        @Schema(description = "다음 페이지가 있을 경우 값=1, 없을 경우 = 0")
        private boolean hasNext;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class performanceDto {

        @Schema(description = "아이디")
        Long ticketInfoId;

        @Schema(description = "공연 제목")
        String title;

        @Schema(description = "공연 설명", example="#스물다섯_스물하나 #데이식스 #잔나비 #YB밴드 #백예린 #미도와_파라솔 ")
        String content;

        @Schema(description = "공연 포스터 이미지")
        String posterUrl;

        @Schema(description = "공연 상태")
        PerformanceStatus status;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class performanceInfoDto{
        PerformanceResponse performanceResponse;

        @Schema(description = "공연 상태")
        PerformanceStatus status;
    }
}
