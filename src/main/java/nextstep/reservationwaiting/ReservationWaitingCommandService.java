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
    private final ReservationWaitingUpdateService reservationWaitingUpdateService;
    private final ReservationCreateService reservationCreateService;
    private final ReservationReadService reservationReadService;

    public ReservationWaitingCommandService(
        ReservationWaitingCreateService reservationWaitingCreateService,
        ReservationWaitingUpdateService reservationWaitingUpdateService,
        ReservationCreateService reservationCreateService,
        ReservationReadService reservationReadService
    ) {
        this.reservationWaitingCreateService = reservationWaitingCreateService;
        this.reservationWaitingUpdateService = reservationWaitingUpdateService;
        this.reservationCreateService = reservationCreateService;
        this.reservationReadService = reservationReadService;
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

    public void cancelById(UserDetails userDetails, Long id) {
        reservationWaitingUpdateService.canceledById(id);
    }
}
