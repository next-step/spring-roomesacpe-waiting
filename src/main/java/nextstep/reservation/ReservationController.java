package nextstep.reservation;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    public final ReservationFacade reservationFacade;
    public final ReservationService reservationService;
    public final ReservationWaitingService reservationWaitingService;

    public ReservationController(ReservationFacade reservationFacade,
        ReservationService reservationService, ReservationWaitingService reservationWaitingService) {
        this.reservationFacade = reservationFacade;
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
        // todo : 예약으로 생성되었을 경우 URI 분기처리 (예약 조회로 가도록)
        Long id = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservations-waitings/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@LoginMember Member member) {
        List<Reservation> results = reservationService.findAllByMember(member);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations-waitings/mine")
    public ResponseEntity readMyReservationsWaitings(@LoginMember Member member) {
        var results = reservationWaitingService.findAllByMemberId(member).reservationWaitings();
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationFacade.deleteReservation(member, id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservations-watings/{scheduleId}")
    public ResponseEntity deleteReservationWaitings(@LoginMember Member member, @PathVariable Long scheduleId) {
        reservationWaitingService.deleteById(member, scheduleId);

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
