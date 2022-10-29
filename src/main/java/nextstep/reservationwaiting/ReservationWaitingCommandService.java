package nextstep.reservationwaiting;

import auth.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationWaitingCommandService {

    private final ReservationWaitingCommandRepository reservationWaitingCommandRepository;

    public ReservationWaitingCommandService(ReservationWaitingCommandRepository reservationWaitingCommandRepository) {
        this.reservationWaitingCommandRepository = reservationWaitingCommandRepository;
    }

    // TODO: 2022/10/29 예약이 없을 경우 자동으로 예약을 생성하도록.
    //  한 계층을 더 만들어서 나누는 것도 괜찮을 것 같다.
    public Long create(UserDetails userDetails, ReservationWaitingRequest request) {
        ReservationWaiting reservationWaiting = new ReservationWaiting(
            request.getScheduleId(),
            userDetails.getId()
        );
        return reservationWaitingCommandRepository.save(reservationWaiting);
    }
}
