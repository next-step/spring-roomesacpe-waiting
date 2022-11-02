package nextstep.admin;

import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
