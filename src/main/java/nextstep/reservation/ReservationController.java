package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationRepresentationService reservationRepresentationService;

    public ReservationController(
        ReservationCommandService reservationCommandService,
        ReservationRepresentationService reservationRepresentationService
    ) {
        this.reservationCommandService = reservationCommandService;
        this.reservationRepresentationService = reservationRepresentationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationCommandService.create(userDetails, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationResponse> results = reservationRepresentationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationRepresentationService.getMyReservations(userDetails);
        return ResponseEntity.ok().body(results);
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity cancel(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationCommandService.cancelById(userDetails, id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
