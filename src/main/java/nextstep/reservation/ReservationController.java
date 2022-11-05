package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetail;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetail userDetail, @RequestBody ReservationRequest reservationRequest) {
        Member member = Member.from(userDetail);
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> readMyReservations(@LoginMember UserDetail userDetail) {
        List<Reservation> results = reservationService.findAllByMember(Member.from(userDetail));
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetail userDetail, @PathVariable Long id) {
        Member member = Member.from(userDetail);
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity cancelReservation(@LoginMember UserDetail userDetail, @PathVariable Long id) {
        Member member = Member.from(userDetail);
        reservationService.cancelReservation(member, id);

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
