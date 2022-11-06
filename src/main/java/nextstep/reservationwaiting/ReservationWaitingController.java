package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.LoginMember;
import java.net.URI;
import java.util.List;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(
        @LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest
    ) {
        URI uri = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(uri)
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.cancelById(member, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaitings(@LoginMember Member member) {
        return ResponseEntity.ok(
            reservationWaitingService.findAllByMember(member)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
