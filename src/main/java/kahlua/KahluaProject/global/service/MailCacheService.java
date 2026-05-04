package kahlua.KahluaProject.global.service;

import jakarta.annotation.PostConstruct;
import kahlua.KahluaProject.domain.apply.entity.ApplyInfo;
import kahlua.KahluaProject.domain.kahluaInfo.entity.LeaderInfo;
import kahlua.KahluaProject.domain.performance.entity.Performance;
import kahlua.KahluaProject.domain.apply.repository.ApplyInfoRepository;
import kahlua.KahluaProject.domain.kahluaInfo.repository.LeaderInfoRepository;
import kahlua.KahluaProject.domain.performance.repository.PerformanceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailCacheService {
    private final LeaderInfoRepository leaderInfoRepository;
    private final ApplyInfoRepository applyInfoRepository;
    private final PerformanceRepository performanceRepository;

    // getter
    @Getter
    private LeaderInfo leaderInfo;
    @Getter
    private ApplyInfo applyInfo;
    @Getter
    private Performance performance;

    @PostConstruct
    public void init() {
        this.leaderInfo = leaderInfoRepository.findTopByOrderByIdDesc().orElse(null);
        this.applyInfo = applyInfoRepository.findTopByOrderByIdDesc().orElse(null);
        this.performance = performanceRepository.findTopByOrderByIdDesc().orElse(null);
    }

    // setter
    public void updateLeaderInfo(LeaderInfo leaderInfo) {
        this.leaderInfo = leaderInfo;
    }

    public void updateApplyInfo(ApplyInfo applyInfo) {
        this.applyInfo = applyInfo;
    }

    public void updatePerformance(Performance performance) {
        this.performance = performance;
    }
}