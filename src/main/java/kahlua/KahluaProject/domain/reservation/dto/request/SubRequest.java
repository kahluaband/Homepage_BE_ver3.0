package kahlua.KahluaProject.domain.reservation.dto.request;

import lombok.Builder;

@Builder
public record SubRequest(
        String type,
        String email,
        String content
) {
}
