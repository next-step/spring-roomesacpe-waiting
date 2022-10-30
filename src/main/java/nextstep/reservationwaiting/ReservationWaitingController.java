package nextstep.reservationwaiting;

import auth.LoginMember;
import java.net.URI;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reservation-waitings")
@RestController
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Void> createReservationWaiting(
        @LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest
    ) {
        Long id = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }
}
