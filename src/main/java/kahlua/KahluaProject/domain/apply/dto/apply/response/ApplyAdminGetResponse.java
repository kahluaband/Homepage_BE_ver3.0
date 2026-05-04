package kahlua.KahluaProject.domain.apply.dto.apply.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kahlua.KahluaProject.domain.apply.entity.Gender;
import kahlua.KahluaProject.domain.apply.entity.Preference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApplyAdminGetResponse {

    private Long id;
    private String name;
    private Gender gender;
    private String birth_date;

    private String phone_num;
    private String address;
    private String major;
    private Preference first_preference;
    private Preference second_preference;

    @Schema(description = "깔루아 지원 동기")
    private String motive;
    @Schema(description = "지원 세션에 대한 경력 및 지원 이유")
    private String experience_and_reason;
    @Schema(description = "이외에 다룰 줄 아는 악기")
    private String play_instrument;
    @Schema(description = "포부 및 각오")
    private String readiness;
}
