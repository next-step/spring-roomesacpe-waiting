package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.UserDetail;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(@LoginMember UserDetail userDetail, @RequestBody ReservationWaitingRequest request) {
        Long id = reservationWaitingService.createReservationWaiting(request.getScheduleId(), Member.from(userDetail));
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("/{reservationWaitingId}")
    public ResponseEntity<Void> deleteReservationWaiting(@PathVariable("reservationWaitingId") Long reservationWaitingId) {
        reservationWaitingService.deleteReservationWaiting(reservationWaitingId);
        return ResponseEntity.noContent().build();
    }
}
