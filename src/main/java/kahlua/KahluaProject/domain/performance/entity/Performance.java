package kahlua.KahluaProject.domain.performance.entity;

import jakarta.persistence.*;
import kahlua.KahluaProject.global.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint")
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String posterImageUrl;

    @Column(nullable = true, columnDefinition = "text")
    private String youtubeUrl;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private PerformanceData performanceData;

    public static Performance create(String posterImageUrl, String youtubeUrl, PerformanceData performanceData) {
        return Performance.builder()
                .posterImageUrl(posterImageUrl)
                .youtubeUrl(youtubeUrl)
                .performanceData(performanceData)
                .build();
    }

    public void update(String posterImageUrl, String youtubeUrl, PerformanceData performanceData) {
        this.posterImageUrl = posterImageUrl;
        this.youtubeUrl = youtubeUrl;
        this.performanceData = performanceData;
    }
}
