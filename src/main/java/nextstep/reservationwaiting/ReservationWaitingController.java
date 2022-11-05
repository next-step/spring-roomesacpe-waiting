package nextstep.reservationwaiting;

import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
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
        CreatedReservation result = reservationWaitingCommandService.create(userDetails, request);
        if (result.getType() == ReservationType.RESERVATION_WAITING) {
            return ResponseEntity.created(URI.create("/reservation-waitings/" + result.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations/" + result.getId())).build();
    }

    @GetMapping("/mine")
    public ResponseEntity getReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaitingDetails> results = reservationWaitingRepresentationService.findAllByMine(userDetails);
        return ResponseEntity.ok().body(results);
    }

    @PutMapping("/{id}")
    public ResponseEntity cancel(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationWaitingCommandService.cancelById(userDetails, id);
        return ResponseEntity.ok().build();
    }
}
