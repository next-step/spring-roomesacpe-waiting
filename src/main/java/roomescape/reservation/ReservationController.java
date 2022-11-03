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

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<ReservationResponse>> readMyReservations(@LoginMemberPrincipal LoginMember loginMember) {
        List<ReservationResponse> results = reservationService.findAllByMemberId(loginMember.getId());
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaitings(@LoginMemberPrincipal LoginMember loginMember) {
        List<ReservationWaitingResponse> results = reservationService.findWaitingsByMemberId(loginMember.getId());
        return ResponseEntity.ok().body(results);
    }

    @PatchMapping("/reservations/{id}/approve")
    public ResponseEntity<Void> approveReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.approveReservation(loginMember.getId(), id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.cancelReservation(loginMember.getId(), id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/{id}/cancel-approve")
    public ResponseEntity<Void> approveCancelReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.approveCancelReservation(loginMember.getId(), id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> cancelReservationWaiting(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.cancelWaiting(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.deleteById(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
