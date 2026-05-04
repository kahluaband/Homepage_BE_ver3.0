package kahlua.KahluaProject.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    GENERAL("일반");

    private final String loginType;
}
