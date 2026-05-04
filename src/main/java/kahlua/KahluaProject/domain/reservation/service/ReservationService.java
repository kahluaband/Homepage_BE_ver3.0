package kahlua.KahluaProject.domain.reservation.service;

import jakarta.transaction.Transactional;
import kahlua.KahluaProject.domain.user.service.UserService;
import kahlua.KahluaProject.global.apipayload.code.status.ErrorStatus;
import kahlua.KahluaProject.domain.reservation.entity.Reservation;
import kahlua.KahluaProject.domain.reservation.entity.ReservationStatus;
import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.reservation.dto.request.ReservationProceedRequest;
import kahlua.KahluaProject.domain.reservation.dto.request.ReservationRequest;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationListResponse;
import kahlua.KahluaProject.domain.reservation.dto.response.ReservationResponse;
import kahlua.KahluaProject.global.exception.GeneralException;
import kahlua.KahluaProject.domain.reservation.repository.ReservationRepository;
import kahlua.KahluaProject.global.websocket.WebSocketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kahlua.KahluaProject.domain.reservation.converter.ReservationConverter.*;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    public ReservationResponse proceed(ReservationProceedRequest reservationProceedRequest, String date, Map<String, Object> header) {

        String email = getValueFromHeader(header, "email");

        return ReservationResponse.builder()
                .email(email)
                .reservationDate(toLocalDate(date))
                .startTime(reservationProceedRequest.startTime())
                .endTime(reservationProceedRequest.endTime())
                .status(ReservationStatus.PROCEEDING)
                .build();
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, String date, Map<String, Object> header) {

        if (reservationRepository.existByDateAndTime(toLocalDate(date), reservationRequest.startTime(), reservationRequest.endTime())) {
            throw new WebSocketException("해당 시간에 예약내역이 존재합니다.");
        }

        String email = getValueFromHeader(header, "email");
        User user = userService.getUserByEmail(email);

        Reservation reservation = toReservation(reservationRequest, user, toLocalDate(date), ReservationStatus.RESERVED);
        Reservation savedReservation = reservationRepository.save(reservation);

        return toReservationResponse(savedReservation, email);
    }

    @Transactional
    public ReservationListResponse getByDate(LocalDate date) {

        List<Reservation> reservationList = reservationRepository.findByDate(date);

        List<ReservationResponse> reservationResponseList = reservationList.stream()
                .map(reservation -> toReservationResponse(reservation, reservation.getUser().getEmail()))
                .collect(Collectors.toList());

        return new ReservationListResponse(reservationResponseList);
    }

    public ReservationListResponse getByUser(User user) {

        List<Reservation> reservationList = reservationRepository.findByUserOrderByReservationDateDescStartTimeAsc(user);
        List<ReservationResponse> reservationResponseList = reservationList.stream()
                .map(reservation -> toReservationResponse(reservation, user.getEmail()))
                .collect(Collectors.toList());

        return new ReservationListResponse(reservationResponseList);
    }

    @Transactional
    public void delete(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservationRepository.delete(reservation);
    }

    // String to LocalDateTime
    private LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private String getValueFromHeader(Map<String, Object> header, String key) {
        return (String)header.get(key);
    }

}
