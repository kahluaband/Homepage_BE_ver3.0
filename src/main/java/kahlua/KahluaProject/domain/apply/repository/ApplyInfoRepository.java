package kahlua.KahluaProject.domain.apply.repository;

import kahlua.KahluaProject.domain.apply.entity.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, Long> {
        Optional<ApplyInfo> findTopByOrderByIdDesc();
}
