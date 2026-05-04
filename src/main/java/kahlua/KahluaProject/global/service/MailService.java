package kahlua.KahluaProject.global.service;

import jakarta.mail.internet.MimeMessage;
import kahlua.KahluaProject.domain.apply.entity.Apply;
import kahlua.KahluaProject.domain.apply.entity.Preference;
import kahlua.KahluaProject.domain.apply.entity.ApplyInfo;
import kahlua.KahluaProject.domain.kahluaInfo.entity.LeaderInfo;
import kahlua.KahluaProject.domain.performance.entity.Performance;
import kahlua.KahluaProject.domain.ticket.entity.Ticket;
import kahlua.KahluaProject.domain.ticket.entity.Type;
import kahlua.KahluaProject.domain.user.service.ParticipantsService;
import kahlua.KahluaProject.global.utils.TimeFormatUtils;
import kahlua.KahluaProject.domain.performance.service.PerformanceService;
import kahlua.KahluaProject.domain.performance.entity.PerformanceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.time.format.DateTimeFormatter;
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final ParticipantsService participantsService;
    private final MailCacheService mailCacheService;
    private final PerformanceService performanceService;

    @Async
    public void sendApplicantEmail(Apply apply){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        LeaderInfo leaderInfo = mailCacheService.getLeaderInfo();
        ApplyInfo applyInfo = mailCacheService.getApplyInfo();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(apply.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("kahluaband.com 에서 발송한 메일입니다."); // 메일 제목
            mimeMessageHelper.setText(setApplicantContext(apply, leaderInfo, applyInfo), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    // thymeleaf를 통한 html 적용
    public String setApplicantContext(Apply apply, LeaderInfo leaderInfo, ApplyInfo applyInfo) {

        Context context = new Context();
        context.setVariable("name", apply.getName());
        context.setVariable("phoneNum", apply.getPhoneNum());
        context.setVariable("leaderName", leaderInfo.getLeaderName());
        context.setVariable("leaderEmail", leaderInfo.getLeaderEmail());
        context.setVariable("leaderPhoneNum", leaderInfo.getLeaderPhoneNum());
        context.setVariable("yearLevel", leaderInfo.getTerm());
        context.setVariable("auditionDate", applyInfo.getApplyInfoData().interviewSchedule().format(DateTimeFormatter.ofPattern("M월 d일")));

        if (apply.getFirstPreference() == Preference.VOCAL) {
            context.setVariable("vocalVideoDeadline", applyInfo.getApplyInfoData().vocalEndDate().format(DateTimeFormatter.ofPattern("M월 d일")));
            return templateEngine.process("applicantVocal", context);
        }
        else {
            return templateEngine.process("applicantOther", context);
        }
    }

    @Async
    public void sendTicketEmail(Ticket ticket){

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        LeaderInfo leaderInfo = mailCacheService.getLeaderInfo();
        Performance performance = mailCacheService.getPerformance();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(ticket.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("kahluaband.com 에서 발송한 메일입니다."); // 메일 제목
            mimeMessageHelper.setText(setTicketContext(ticket, leaderInfo, performance), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("Succeeded to send Email");
        } catch (Exception e) {
            log.info("Failed to send Email");
            throw new RuntimeException(e);
        }
    }

    // thymeleaf를 통한 html 적용
    public String setTicketContext(Ticket ticket, LeaderInfo leaderInfo, Performance performance) {

        Context context = new Context();
        context.setVariable("buyer", ticket.getBuyer());
        context.setVariable("phoneNum", ticket.getPhone_num());
        context.setVariable("reservationId", ticket.getReservationId());
        context.setVariable("leaderName", leaderInfo.getLeaderName());
        context.setVariable("leaderPhoneNum", leaderInfo.getLeaderPhoneNum());

        if (performance == null || performance.getPerformanceData() == null) {
            performance = performanceService.getLatestPerformance(); // 캐시에 공연 정보가 없을 경우 최신 공연 정보로 대체
        }
        PerformanceData data = performance.getPerformanceData();
        context.setVariable("performanceMonth", TimeFormatUtils.toMonthKST(data.performanceStartTime()));
        context.setVariable("performanceStartDate", TimeFormatUtils.toDateKST(data.performanceStartTime()));
        context.setVariable("performanceStartTime", TimeFormatUtils.toTimeKST(data.performanceStartTime()));
        context.setVariable("performanceEndTime", TimeFormatUtils.toTimeKST(data.performanceEndTime()));
        context.setVariable("entranceTime", TimeFormatUtils.toTimeKST(data.entranceTime()));
        context.setVariable("venue", data.venue());
        context.setVariable("address", data.address());

        if (ticket.getType() == Type.GENERAL) {
            context.setVariable("count", participantsService.getTotalGeneralTicket(ticket.getId()));
            return templateEngine.process("ticketGeneral", context);
        }
        else  {
            context.setVariable("studentId", ticket.getStudentId());
            return templateEngine.process("ticketFreshman", context);
        }
    }
}