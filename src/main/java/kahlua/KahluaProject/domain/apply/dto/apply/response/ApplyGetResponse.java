package kahlua.KahluaProject.domain.apply.dto.apply.response;

import kahlua.KahluaProject.domain.apply.entity.Gender;
import kahlua.KahluaProject.domain.apply.entity.Preference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApplyGetResponse {

    private Long id;
    private String name;
    private String phone_num;
    private String birth_date;

    private Gender gender;

    private String address;

    private String major;

    private Preference first_preference;
    private Preference second_preference;

    private String experience_and_reason;
    private String play_instrument;
    private String motive;
    private String finish_time;
    private Boolean meeting;
    private String readiness;
}
