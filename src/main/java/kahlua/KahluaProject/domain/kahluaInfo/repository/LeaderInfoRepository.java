package kahlua.KahluaProject.domain.kahluaInfo.repository;

import kahlua.KahluaProject.domain.kahluaInfo.entity.LeaderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaderInfoRepository extends JpaRepository<LeaderInfo, Long> {
    Optional<LeaderInfo> findTopByOrderByIdDesc();
}