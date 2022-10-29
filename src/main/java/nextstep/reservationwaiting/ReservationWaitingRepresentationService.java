package nextstep.reservationwaiting;

import auth.UserDetails;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReservationWaitingRepresentationService {

    private final ReservationWaitingRepresentationRepository reservationWaitingRepresentationRepository;

    public ReservationWaitingRepresentationService(ReservationWaitingRepresentationRepository reservationWaitingRepresentationRepository) {
        this.reservationWaitingRepresentationRepository = reservationWaitingRepresentationRepository;
    }

    public List<ReservationWaitingDetails> findAllByMine(UserDetails userDetails) {
        return reservationWaitingRepresentationRepository.findAllNotHideWaitingDetailsByMemberId(userDetails.getId());
    }
}
