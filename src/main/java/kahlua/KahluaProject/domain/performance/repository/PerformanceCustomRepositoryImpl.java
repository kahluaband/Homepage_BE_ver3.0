package kahlua.KahluaProject.domain.performance.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kahlua.KahluaProject.domain.performance.entity.Performance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static kahlua.KahluaProject.domain.performance.entity.QPerformance.performance;


@Repository
@RequiredArgsConstructor
public class PerformanceCustomRepositoryImpl implements PerformanceCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Performance> findPerformances(int limit) {
        return jpaQueryFactory
                .selectFrom(performance)
                .orderBy(
                        Expressions.stringTemplate("JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time'))", performance.performanceData)
                                .desc()
                )
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Performance> findPerformancesOrderByDateTime(LocalDateTime cursorDateTime, int limit) {
        DateTimeExpression<LocalDateTime> dbDateTimeExpr = Expressions.dateTimeTemplate(
                LocalDateTime.class,
                "STR_TO_DATE(CONCAT("
                        + "CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[0]'))), '-',"
                        + "LPAD(CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[1]'))), 2, '0'), '-',"
                        + "LPAD(CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[2]'))), 2, '0'), ' ',"
                        + "LPAD(CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[3]'))), 2, '0'), ':',"
                        + "LPAD(CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[4]'))), 2, '0'), ':',"
                        + "LPAD(CONCAT('', JSON_UNQUOTE(JSON_EXTRACT({0}, '$.performance_start_time[5]'))), 2, '0')"
                        + "), '%Y-%m-%d %H:%i:%s')",
                performance.performanceData
        );

        String cursorFormatted = cursorDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        DateTimeExpression<LocalDateTime> cursorDateTimeExpr = Expressions.dateTimeTemplate(
                LocalDateTime.class,
                "STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
                Expressions.constant(cursorFormatted)
        );

        return jpaQueryFactory
                .selectFrom(performance)
                .where(dbDateTimeExpr.lt(cursorDateTimeExpr))
                .orderBy(dbDateTimeExpr.desc())
                .limit(limit)
                .fetch();
    }

}
