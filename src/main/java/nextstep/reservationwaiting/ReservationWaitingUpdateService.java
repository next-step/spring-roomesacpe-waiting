package nextstep.reservationwaiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationWaitingUpdateService {

    private final ReservationWaitingCommandRepository reservationWaitingCommandRepository;

    public ReservationWaitingUpdateService(ReservationWaitingCommandRepository reservationWaitingCommandRepository) {
        this.reservationWaitingCommandRepository = reservationWaitingCommandRepository;
    }

    public void canceledById(Long memberId, Long reservationId) {
        ReservationWaiting reservationWaiting = reservationWaitingCommandRepository.findById(reservationId);
        reservationWaiting.cancelIfOwner(memberId);
        reservationWaitingCommandRepository.updateStatus(reservationWaiting);
    }
}
