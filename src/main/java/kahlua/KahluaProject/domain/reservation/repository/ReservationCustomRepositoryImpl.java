package kahlua.KahluaProject.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kahlua.KahluaProject.domain.reservation.entity.Reservation;
import kahlua.KahluaProject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static kahlua.KahluaProject.domain.reservation.entity.QReservation.reservation;


@Repository
@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reservation> findByDate(LocalDate date) {

        return jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.reservationDate.eq(date))
                .orderBy(reservation.startTime.asc())
                .fetch();
    }

    @Override
    public List<Reservation> findByUser(User user) {

        return jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.user.eq(user))
                .orderBy(reservation.reservationDate.asc(), reservation.startTime.asc())
                .fetch();
    }

    @Override
    public Boolean existByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return jpaQueryFactory
                .selectOne()
                .from(reservation)
                .where(reservation.reservationDate.eq(date)
                        .and(reservation.startTime.lt(endTime) // 시작 시간이 새로운 예약 종료 시간보다 작고
                        .and(reservation.endTime.gt(startTime)))) // 종료 시간이 새로운 예약 시작 시간보다 큰 경우
                .fetchFirst() != null;
    }
}
