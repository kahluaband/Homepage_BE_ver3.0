package kahlua.KahluaProject.domain.reservation.repository;

import kahlua.KahluaProject.domain.reservation.entity.Reservation;
import kahlua.KahluaProject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {
    List<Reservation> findByUserOrderByReservationDateDescStartTimeAsc(User user);
}
