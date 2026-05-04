package kahlua.KahluaProject.domain.ticket;

import kahlua.KahluaProject.domain.ticket.entity.Meeting;
import kahlua.KahluaProject.domain.ticket.entity.Participants;
import kahlua.KahluaProject.domain.ticket.entity.Ticket;

import java.util.ArrayList;
import java.util.List;

import static kahlua.KahluaProject.domain.ticket.entity.Type.FRESHMAN;
import static kahlua.KahluaProject.domain.ticket.entity.Type.GENERAL;

// 모든 테스트에서 일관되게 사용할 수 있는 mock data를 만드는 클래스
public class TicketUtilTest {

    public static Ticket createMockGeneralTicket() {
        Ticket generalTicket = Ticket.builder()
                .buyer("kahlua")
                .phone_num("010-1234-5678")
                .type(GENERAL)
                .build();

        return generalTicket;
    }

    public static Participants createMockParticipant(String name, String phone_num, Ticket ticket) {
        return Participants.builder()
                .name(name)
                .phone_num(phone_num)
                .ticket(ticket)
                .build();
    }

    public static List<Participants> createMockParticipants(Ticket ticket) {
        List<Participants> participants = new ArrayList<>();
        participants.add(createMockParticipant("kahlua1", "010-1234-5678", ticket));
        participants.add(createMockParticipant("kahlua2", "010-5678-1234", ticket));
        return participants;
    }

    public static Ticket createMockFreshmanTicket() {
        Ticket freshmanTicket = Ticket.builder()
                .buyer("kahlua")
                .phone_num("010-1234-5678")
                .type(FRESHMAN)
                .major("COMPUTER")
                .studentId("C511111")
                .meeting(Meeting.DAY1_ATTEND)
                .build();

        return freshmanTicket;
    }
}