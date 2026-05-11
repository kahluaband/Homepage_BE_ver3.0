package kahlua.KahluaProject.domain.album.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FOUNDATION_FESTIVAL("창립제"),
    YEAR_END_PARTY("송년회"),
    PERFORMANCE("공연"),
    ETC("기타");

    private final String description;
}