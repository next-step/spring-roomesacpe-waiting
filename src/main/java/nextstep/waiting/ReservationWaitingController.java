package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ReservationWaitingController {

    public final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationWaitingRequest request) {
        Long id = reservationWaitingService.create(member, request);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
