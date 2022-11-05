package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;
    public final ReservationWaitingService reservationWaitingService;

    public ReservationController(ReservationService reservationService,
        ReservationWaitingService reservationWaitingService) {
        this.reservationService = reservationService;
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @PostMapping("/reservations-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservations-waitings/" + id)).build();
    }


    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations-waitings")
    public ResponseEntity readReservationsWaitings(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationWaitingService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservations-watings/{id}")
    public ResponseEntity deleteReservationWaitings(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
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
