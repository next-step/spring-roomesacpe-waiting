package nextstep.reservation;

import static java.util.stream.Collectors.toList;

import auth.UserDetails;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReservationRepresentationService {

    private final ReservationReadService reservationReadService;

    public ReservationRepresentationService(ReservationReadService reservationReadService) {
        this.reservationReadService = reservationReadService;
    }

    public List<ReservationResponse> getMyReservations(UserDetails userDetails) {
        return reservationReadService.findAllByMemberId(userDetails.getId())
            .stream()
            .map(ReservationResponse::from)
            .collect(toList());
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        return reservationReadService.findAllByThemeIdAndDate(themeId, date)
            .stream()
            .map(ReservationResponse::from)
            .collect(toList());
    }
}
