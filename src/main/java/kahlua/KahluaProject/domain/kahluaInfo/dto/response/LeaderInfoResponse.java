package kahlua.KahluaProject.domain.kahluaInfo.dto.response;

import lombok.Builder;


@Builder
public record LeaderInfoResponse (
        String leaderName,
        String phoneNumber,
        String email,
        Long term
) {}