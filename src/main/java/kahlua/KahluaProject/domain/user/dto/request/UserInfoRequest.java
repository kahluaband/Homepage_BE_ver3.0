package kahlua.KahluaProject.domain.user.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoRequest (
    @Schema(description = "이름", example = "김깔룽")
    String name,

    @Schema(description = "기수", example = "21")
    Long term,

    @Schema( description= "세션", example = "VOCAL | BASS | GUITAR | DRUM | SYNTHESIZER | MANAGER")
    String session
    ){}
