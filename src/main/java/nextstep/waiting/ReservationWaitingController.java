package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

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

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.findMine(member);
        return ResponseEntity.ok().body(results);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
