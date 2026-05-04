package kahlua.KahluaProject.domain.ticket.repository;

import kahlua.KahluaProject.domain.ticket.entity.Ticket;
import kahlua.KahluaProject.domain.ticket.entity.Type;

import java.util.List;

public interface TicketCustomRepository {

    List<Ticket> findAllOrderByStatus();
    List<Ticket> findAllByTypeOrderByStatus(Type type);
}
