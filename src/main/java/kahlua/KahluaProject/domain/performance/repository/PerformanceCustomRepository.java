package kahlua.KahluaProject.domain.performance.repository;

import kahlua.KahluaProject.domain.performance.entity.Performance;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceCustomRepository {
    List<Performance> findPerformances(int limit);

    List<Performance> findPerformancesOrderByDateTime(LocalDateTime dateTime, int limit);
}
