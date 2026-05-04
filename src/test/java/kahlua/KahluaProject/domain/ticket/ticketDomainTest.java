package kahlua.KahluaProject.domain.ticket;

import kahlua.KahluaProject.domain.ticket.entity.Meeting;
import kahlua.KahluaProject.domain.ticket.entity.Participants;
import kahlua.KahluaProject.domain.ticket.entity.Ticket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kahlua.KahluaProject.domain.ticket.entity.Type.FRESHMAN;
import static kahlua.KahluaProject.domain.ticket.entity.Type.GENERAL;

@DisplayName("Ticket 도메인 테스트")
public class ticketDomainTest {

    @Test
    @DisplayName("일반티켓이 생성되는지 확인하는 테스트")
    void createGeneralTicket() {
        //given
        Ticket mockTicket = TicketUtilTest.createMockGeneralTicket();

        List<Participants> mockParticipants = TicketUtilTest.createMockParticipants(mockTicket);

        //when, then
        Assertions.assertThat(mockTicket.getBuyer()).isEqualTo("kahlua");
        Assertions.assertThat(mockTicket.getPhone_num()).isEqualTo("010-1234-5678");
        Assertions.assertThat(mockTicket.getType()).isEqualTo(GENERAL);

        Assertions.assertThat(mockParticipants).hasSize(2);

        Participants participants1 = mockParticipants.get(0);
        Assertions.assertThat(participants1.getName()).isEqualTo("kahlua1");
        Assertions.assertThat(participants1.getPhone_num()).isEqualTo("010-1234-5678");
        Assertions.assertThat(participants1.getTicket()).isEqualTo(mockTicket);

        Participants participants2 = mockParticipants.get(1);
        Assertions.assertThat(participants2.getName()).isEqualTo("kahlua2");
        Assertions.assertThat(participants2.getPhone_num()).isEqualTo("010-5678-1234");
        Assertions.assertThat(participants1.getTicket()).isEqualTo(mockTicket);
    }

    @Test
    @DisplayName("신입생티켓이 생성되는지 확인하는 테스트")
    void createFreshmanTicket() {
        //given
        Ticket freshmanTicket = TicketUtilTest.createMockFreshmanTicket();

        //when, then
        Assertions.assertThat(freshmanTicket.getBuyer()).isEqualTo("kahlua");
        Assertions.assertThat(freshmanTicket.getPhone_num()).isEqualTo("010-1234-5678");
        Assertions.assertThat(freshmanTicket.getType()).isEqualTo(FRESHMAN);
        Assertions.assertThat(freshmanTicket.getMajor()).isEqualTo("COMPUTER");
        Assertions.assertThat(freshmanTicket.getStudentId()).isEqualTo("C511111");
        Assertions.assertThat(freshmanTicket.getMeeting()).isEqualTo(Meeting.DAY1_ATTEND);
    }
}
