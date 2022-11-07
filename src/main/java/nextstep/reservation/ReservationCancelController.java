package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationCancelController {

    public final ReservationCancelService reservationCancelService;

    public ReservationCancelController(ReservationCancelService reservationCancelService) {
        this.reservationCancelService = reservationCancelService;
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationCancelService.cancel(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/{id}/cancel-approve")
    public ResponseEntity<List<ReservationResponse>> cancelApproveReservation(@LoginMember(isAdmin = true) Member member, @PathVariable Long id) {
        reservationCancelService.cancelApprove(id);
        return ResponseEntity.ok().build();
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
