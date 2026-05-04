package kahlua.KahluaProject.domain.reservation.converter;

import kahlua.KahluaProject.domain.reservation.entity.Reservation;
import kahlua.KahluaProject.domain.reservation.entity.ReservationStatus;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.reservation.dto.request.ReservationRequest;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationResponse;

import java.time.LocalDate;

public class ReservationConverter {


    public static Reservation toReservation(ReservationRequest reservationRequest, User user, LocalDate reservationDate, ReservationStatus status) {

        return Reservation.builder()
                .user(user)
                .type(reservationRequest.type())
                .clubRoomUsername(reservationRequest.clubroomUsername())
                .reservationDate(reservationDate)
                .startTime(reservationRequest.startTime())
                .endTime(reservationRequest.endTime())
                .status(status)
                .build();
    }
    public static ReservationResponse toReservationResponse(Reservation reservation, String email) {

        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .email(email)
                .type(reservation.getType())
                .clubroomUsername(reservation.getClubRoomUsername())
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .build();


    }
}
