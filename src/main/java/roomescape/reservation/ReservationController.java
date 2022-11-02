package roomescape.reservation;

import auth.LoginMember;
import auth.LoginMemberPrincipal;
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
    public ResponseEntity<Void> createReservation(@LoginMemberPrincipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(loginMember.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createReservationWaiting(@LoginMemberPrincipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createWaiting(loginMember.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.deleteById(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.deleteWaiting(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
