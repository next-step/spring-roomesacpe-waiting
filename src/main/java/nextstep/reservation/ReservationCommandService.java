package nextstep.reservation;

import auth.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCommandService {

    private final ReservationCreateService reservationCreateService;
    private final ReservationUpdateService reservationUpdateService;

    public ReservationCommandService(
        ReservationCreateService reservationCreateService,
        ReservationUpdateService reservationUpdateService
    ) {
        this.reservationCreateService = reservationCreateService;
        this.reservationUpdateService = reservationUpdateService;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        return reservationCreateService.create(userDetails.getId(), reservationRequest.getScheduleId());
    }

    public void deleteById(UserDetails userDetails, Long reservationId) {
        reservationUpdateService.hideById(userDetails.getId(), reservationId);
    }
}
