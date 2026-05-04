package kahlua.KahluaProject.domain.performance.service;

import kahlua.KahluaProject.domain.performance.entity.Performance;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.performance.converter.PerformanceConverter;
import kahlua.KahluaProject.domain.performance.entity.PerformanceStatus;
import kahlua.KahluaProject.domain.performance.dto.request.PerformanceRequest;
import kahlua.KahluaProject.domain.performance.dto.response.PerformanceListResponse;
import kahlua.KahluaProject.domain.performance.dto.response.PerformanceResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.global.service.MailCacheService;
import kahlua.KahluaProject.domain.performance.repository.PerformanceRepository;
import kahlua.KahluaProject.domain.performance.entity.PerformanceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static kahlua.KahluaProject.domain.performance.entity.PerformanceStatus.CLOSED;
import static kahlua.KahluaProject.domain.performance.entity.PerformanceStatus.OPEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final MailCacheService mailCacheService;

    public PerformanceListResponse.performanceListDto getPerformances(Long cursor, int limit){

        List<Performance> performances;
        if (cursor == null || cursor == 0) {
            performances = performanceRepository.findPerformances(limit+1);
        } else {
            Performance cursorPerformance = performanceRepository.findById(cursor)
                    .orElseThrow(()->new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
            LocalDateTime cursorDateTime= cursorPerformance.getPerformanceData().performanceStartTime();
            performances = performanceRepository.findPerformancesOrderByDateTime(cursorDateTime, limit+1);
        }

        Long nextCursor = null;
        boolean hasNext= performances.size() > limit;

        if (hasNext){
            Performance lastPerformance = performances.get(limit-1);
            nextCursor= lastPerformance.getId();
            performances = performances.subList(0,limit);
        }

        List<PerformanceListResponse.performanceDto> ticketInfoDtos= performances.stream()
                .map(ticketInfo -> PerformanceConverter.toPerformanceSummaryDto(ticketInfo,checkStatus(ticketInfo)))
                .collect(Collectors.toList());

        return PerformanceListResponse.performanceListDto.builder()
                .performances(ticketInfoDtos)
                .nextcursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    public PerformanceStatus checkStatus(Performance performance) {
        if (LocalDateTime.now().isAfter(performance.getPerformanceData().bookingEndDate()) || LocalDateTime.now().isBefore(performance.getPerformanceData().bookingStartDate())) {
            return CLOSED;
        } else{return OPEN;}
    }

    public Performance getLatestPerformance() {
        return performanceRepository.findTopByOrderByIdDesc()
                .orElse(null);
    }

    public PerformanceListResponse.performanceInfoDto getPerformanceInfo(Long ticketInfoId){
        Performance performance = performanceRepository.findById(ticketInfoId)
                .orElseThrow(()->new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));

        return PerformanceListResponse.performanceInfoDto.builder()
                .performanceResponse(PerformanceConverter.toPerformanceDto(performance))
                .status(checkStatus(performance))
                .build();
    }

    public PerformanceListResponse.performanceInfoDto getLatestPerformanceInfo(){
        Performance performance = performanceRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(()->new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));

        return PerformanceListResponse.performanceInfoDto.builder()
                .performanceResponse(PerformanceConverter.toPerformanceDto(performance))
                .status(checkStatus(performance))
                .build();
    }

    public PerformanceResponse createPerformance(PerformanceRequest request) {

        String posterImageUrl = request.posterImageUrl();
        String youtubeUrl = request.youtubeUrl();
        PerformanceData performanceData = PerformanceConverter.toPerformance(request);
        Performance performance = Performance.create(posterImageUrl, youtubeUrl, performanceData);
        performanceRepository.save(performance);
        mailCacheService.updatePerformance(performance);
        return PerformanceConverter.toPerformanceDto(performance);
    }

    public void deletePerformance(Long ticketInfoId) {
        Performance performance=performanceRepository.findById(ticketInfoId)
                .orElseThrow(()->new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));

        performanceRepository.delete(performance);
    }
}
