package nextstep.reservation;

import auth.UserDetails;
import nextstep.sales.SalesCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCommandService {

    private final ReservationCreateService reservationCreateService;
    private final ReservationUpdateService reservationUpdateService;
    private final SalesCreateService salesCreateService;

    public ReservationCommandService(
        ReservationCreateService reservationCreateService,
        ReservationUpdateService reservationUpdateService,
        SalesCreateService salesCreateService
    ) {
        this.reservationCreateService = reservationCreateService;
        this.reservationUpdateService = reservationUpdateService;
        this.salesCreateService = salesCreateService;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        return reservationCreateService.create(userDetails.getId(), reservationRequest.getScheduleId());
    }

    public void approveReservation(Long reservationId) {
        reservationUpdateService.approveById(reservationId);
        salesCreateService.createByReservationApprove(reservationId);
    }

    public void cancelReservation(UserDetails userDetails, Long reservationId) {
        reservationUpdateService.cancelById(userDetails.getId(), reservationId);
    }
}
