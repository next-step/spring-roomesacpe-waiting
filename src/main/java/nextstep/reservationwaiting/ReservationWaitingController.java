package nextstep.reservationwaiting;

import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservation-waitings")
@RestController
public class ReservationWaitingController {

    private final ReservationWaitingCommandService reservationWaitingCommandService;
    private final ReservationWaitingRepresentationService reservationWaitingRepresentationService;

    public ReservationWaitingController(
        ReservationWaitingCommandService reservationWaitingCommandService,
        ReservationWaitingRepresentationService reservationWaitingRepresentationService
    ) {
        this.reservationWaitingCommandService = reservationWaitingCommandService;
        this.reservationWaitingRepresentationService = reservationWaitingRepresentationService;
    }

    @PostMapping
    public ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest request) {
        Long id = reservationWaitingCommandService.create(userDetails, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping
    public ResponseEntity getReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaitingDetails> results = reservationWaitingRepresentationService.findAllByMine(userDetails);
        return ResponseEntity.ok().body(results);
    }
}
