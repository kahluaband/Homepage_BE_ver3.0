package kahlua.KahluaProject.domain.ticket.repository;

import kahlua.KahluaProject.domain.ticket.entity.Status;
import kahlua.KahluaProject.domain.ticket.entity.Ticket;
import kahlua.KahluaProject.domain.ticket.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//데이터베이스와의 상호작용을 담당. 데이터 저장, 조회, 수정, 삭제와 같은 작업을 처리
public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketCustomRepository {

    boolean existsByReservationId(String reservationId);
    // List<Ticket> findAllByOrderByIdDesc();
    // List<Ticket> findAllByTypeOrderByIdDesc(Type type);
    Optional<Ticket> findByReservationId(String reservationId);
    List<Ticket> findAllByOrderByBuyerAscIdDesc();
    List<Ticket> findAllByTypeOrderByBuyerAscIdDesc(Type type);

    Optional<Long> countByStatusAndDeletedAtIsNull(Status status);
    Optional<Long>  countByTypeAndDeletedAtIsNull(Type type);
    Optional<Long>  countAllByDeletedAtIsNull();
    Optional<Long>  countByTypeAndStatusAndDeletedAtIsNull(Type type, Status status);
}
