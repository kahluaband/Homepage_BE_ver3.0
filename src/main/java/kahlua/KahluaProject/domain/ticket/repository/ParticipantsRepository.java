package kahlua.KahluaProject.domain.ticket.repository;

import kahlua.KahluaProject.domain.ticket.entity.Participants;
import kahlua.KahluaProject.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantsRepository extends JpaRepository<Participants, Long> {

    List<Participants> findByTicket(Ticket ticket);
    Integer countByTicket_Id(Long id);
}
