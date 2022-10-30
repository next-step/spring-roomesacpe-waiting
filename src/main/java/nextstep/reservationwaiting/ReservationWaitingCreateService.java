package nextstep.reservationwaiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationWaitingCreateService {

    private final ReservationWaitingCommandRepository reservationWaitingCommandRepository;

    public ReservationWaitingCreateService(ReservationWaitingCommandRepository reservationWaitingCommandRepository) {
        this.reservationWaitingCommandRepository = reservationWaitingCommandRepository;
    }

    public Long create(Long memberId, Long scheduleId) {
        ReservationWaiting reservationWaiting = new ReservationWaiting(scheduleId, memberId);
        return reservationWaitingCommandRepository.save(reservationWaiting);
    }
}
