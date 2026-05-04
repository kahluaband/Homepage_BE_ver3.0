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
public class ApplyItemResponse {

    @Schema(description = "지원자 id")
    private Long id;

    @Schema(description = "지원자 이름")
    private String name;

    @Schema(description = "지원자 전화번호")
    private String phone_num;

    @Schema(description = "지원자 생년월일")
    private String birth_date;

    @Schema(description = "지원자 성별")
    private Gender gender;

    @Schema(description = "지원자 주소")
    private String address;

    @Schema(description = "지원자 전공")
    private String major;

    @Schema(description = "지원자 1지망 악기")
    private Preference first_preference;

    @Schema(description = "지원자 2지망 악기")
    private Preference second_preference;
}
