package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingCommandRepository;
import nextstep.sales.SalesCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCommandService {

    private final ReservationCreateService reservationCreateService;
    private final ReservationUpdateService reservationUpdateService;
    private final ReservationWaitingCommandRepository reservationWaitingCommandRepository;
    private final SalesCreateService salesCreateService;

    public ReservationCommandService(
        ReservationCreateService reservationCreateService,
        ReservationUpdateService reservationUpdateService,
        ReservationWaitingCommandRepository reservationWaitingCommandRepository,
        SalesCreateService salesCreateService
    ) {
        this.reservationCreateService = reservationCreateService;
        this.reservationUpdateService = reservationUpdateService;
        this.reservationWaitingCommandRepository = reservationWaitingCommandRepository;
        this.salesCreateService = salesCreateService;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        return reservationCreateService.create(userDetails.getId(), reservationRequest.getScheduleId());
    }

    public void approveReservation(UserDetails userDetails, Long reservationId) {
        if (userDetails.checkNotAdmin()) {
            throw new AuthenticationException();
        }
        reservationUpdateService.approveById(reservationId);
        salesCreateService.createByReservationApprove(reservationId);
    }

    public void cancelReservation(UserDetails userDetails, Long reservationId) {
        if (userDetails.checkNotAdmin()) {
            userCancelReservation(userDetails, reservationId);
        } else {
            adminCancelReservation(reservationId);
        }
    }

    private void userCancelReservation(UserDetails userDetails, Long reservationId) {
        Reservation reservation = reservationUpdateService.cancelById(
            userDetails.getId(),
            reservationId
        );
        if (reservation.isWithDrawOrCancel()) {
            salesCreateService.createRefundByReservation(reservation);
        }
        promoteWaitingIfExists(reservation);
    }

    private void adminCancelReservation(Long reservationId) {
        Reservation reservation = reservationUpdateService.adminCancelById(reservationId);
        salesCreateService.createRefundByReservation(reservation);
        promoteWaitingIfExists(reservation);
    }

    public void cancelApproveReservation(UserDetails userDetails, Long reservationId) {
        if (userDetails.checkNotAdmin()) {
            throw new AuthenticationException();
        }
        Reservation reservation = reservationUpdateService.cancelApproveById(reservationId);
        promoteWaitingIfExists(reservation);
    }

    private void promoteWaitingIfExists(Reservation reservation) {
        ReservationWaiting waiting = reservationWaitingCommandRepository.findFirstWaitingReservationWaitingByScheduleId(reservation.getScheduleId());
        if (waiting != null) {
            waiting.promote();
            reservationWaitingCommandRepository.updateStatus(waiting);
            reservationCreateService.create(waiting.getMemberId(), waiting.getScheduleId());
        }
    }
}
