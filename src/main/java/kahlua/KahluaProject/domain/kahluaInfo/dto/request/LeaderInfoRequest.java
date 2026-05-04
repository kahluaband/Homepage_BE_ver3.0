package kahlua.KahluaProject.domain.kahluaInfo.dto.request;

import lombok.Builder;

@Builder
public record LeaderInfoRequest(
        String leaderName,
        String phoneNumber,
        String email,
        Long term
) {}