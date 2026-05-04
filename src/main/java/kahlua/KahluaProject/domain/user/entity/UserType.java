package kahlua.KahluaProject.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    GENERAL("일반 사용자"), // 정보 입력 안한 경우
    PENDING("대기중인 사용자"), // 정보 입력O + 대기중
    UNACCEPTED("미승인 사용자"), // 정보 입력O + 승인 X
    KAHLUA("깔루아"), // 정보 입력O + 승인 O
    ADMIN("관리자");

    private final String role;
}
