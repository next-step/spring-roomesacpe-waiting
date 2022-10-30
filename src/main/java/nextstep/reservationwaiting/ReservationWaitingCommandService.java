package nextstep.reservationwaiting;

import auth.UserDetails;
import nextstep.reservation.ReservationCreateService;
import nextstep.reservation.ReservationReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationWaitingCommandService {

    private final ReservationWaitingCreateService reservationWaitingCreateService;
    private final ReservationReadService reservationReadService;
    private final ReservationCreateService reservationCreateService;

    public ReservationWaitingCommandService(
        ReservationWaitingCreateService reservationWaitingCreateService,
        ReservationReadService reservationReadService,
        ReservationCreateService reservationCreateService
    ) {
        this.reservationWaitingCreateService = reservationWaitingCreateService;
        this.reservationReadService = reservationReadService;
        this.reservationCreateService = reservationCreateService;
    }

    public CreatedReservation create(UserDetails userDetails, ReservationWaitingRequest request) {
        if (reservationReadService.existsByScheduleId(request.getScheduleId())) {
            return createReservationWaiting(userDetails, request);
        }
        return getCreatedReservation(userDetails, request);
    }

    private CreatedReservation createReservationWaiting(
        UserDetails userDetails,
        ReservationWaitingRequest request
    ) {
        return new CreatedReservation(
            reservationWaitingCreateService.create(userDetails.getId(), request.getScheduleId()),
            ReservationType.RESERVATION_WAITING
        );
    }

    private CreatedReservation getCreatedReservation(
        UserDetails userDetails,
        ReservationWaitingRequest request
    ) {
        return new CreatedReservation(
            reservationCreateService.create(userDetails.getId(), request.getScheduleId()),
            ReservationType.RESERVATION
        );
    }
}
