package nextstep.reservation;

import auth.AuthenticationException;
import java.net.URI;
import java.util.List;
import nextstep.member.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationWaitingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Void> createReservation(
        @LoginMember Member member,
        @RequestBody ReservationRequest request
    ) {
        Long reservationId = reservationService.create(member, request);
        return ResponseEntity.created(URI.create("/reservations/" + reservationId)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(
        @RequestParam Long themeId,
        @RequestParam String date
    ) {
        List<Reservation> reservations = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(reservations);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
        @LoginMember Member member,
        @PathVariable Long id
    ) {
        reservationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> putOnWaitingList(
        @LoginMember Member member,
        @RequestBody ReservationWaitingRequest request
    ) {
        Long waitingId = reservationService.putOnWaitingList(member, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + waitingId)).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
