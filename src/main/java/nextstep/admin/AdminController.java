package nextstep.admin;

import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PatchMapping("/reservations/{reservationId}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.approveReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/{reservationId}/cancel-approve")
    public ResponseEntity<Void> cancelApprove(@PathVariable("reservationId") Long reservationId) {
        reservationService.cancelApprove(reservationId);
        return ResponseEntity.ok().build();
    }
}
