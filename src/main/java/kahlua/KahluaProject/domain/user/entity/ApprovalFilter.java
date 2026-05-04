package kahlua.KahluaProject.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalFilter {
    PENDING("대기"), // 정보 입력O + 대기중
    ALL("전체"),
    APPROVED("승인");

    private final String approvalStatus;
}
