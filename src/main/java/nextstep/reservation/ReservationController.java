package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetail;
import java.net.URI;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberService;
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
    public ResponseEntity createReservation(@LoginMember UserDetail userDetail,
                                            @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(userDetail, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetail userDetail, @PathVariable Long id) {
        reservationService.deleteById(userDetail, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readReservationsMine(@LoginMember UserDetail userDetail) {
        List<ReservationResponse> results = reservationService.findAllByMember(userDetail);
        return ResponseEntity.ok(results);
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
