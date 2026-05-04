package kahlua.KahluaProject.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Session {
    VOCAL("보컬"),
    GUITAR("기타"),
    BASS("베이스"),
    DRUM("드럼"),
    SYNTHESIZER("신디사이저"),
    MANAGER("매니저");

    private final String session;
}
