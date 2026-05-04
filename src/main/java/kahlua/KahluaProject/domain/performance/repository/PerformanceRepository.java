package kahlua.KahluaProject.domain.performance.repository;

import kahlua.KahluaProject.domain.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, PerformanceCustomRepository {
    Optional<Performance> findTopByOrderByCreatedAtDesc();
    Optional<Performance> findTopByOrderByIdDesc();
}
