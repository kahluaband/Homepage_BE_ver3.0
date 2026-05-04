package kahlua.KahluaProject.domain.reservation.repository;

import kahlua.KahluaProject.domain.reservation.entity.Reservation;
import kahlua.KahluaProject.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationCustomRepository {

    List<Reservation> findByDate(LocalDate date);
    List<Reservation> findByUser(User user);
    Boolean existByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime);
}
