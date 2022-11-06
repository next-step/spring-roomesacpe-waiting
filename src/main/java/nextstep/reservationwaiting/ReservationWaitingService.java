package nextstep.reservationwaiting;

import java.net.URI;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationWaitingService {

    private static final String RESERVATION_URI_PREFIX = "/reservations/";
    private static final String RESERVATION_WAITING_URI_PREFIX = "/reservation-waitings/";

    private final ReservationService reservationService;
    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationWaitingService(ReservationService reservationService,
        ReservationWaitingDao reservationWaitingDao) {
        this.reservationService = reservationService;
        this.reservationWaitingDao = reservationWaitingDao;
    }


    @Transactional
    public URI create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Long scheduleId = reservationWaitingRequest.getScheduleId();
        if (reservationService.existsReservation(scheduleId)) {
            return URI.create(
                RESERVATION_WAITING_URI_PREFIX + reservationWaitingDao.save(
                    new ReservationWaiting(scheduleId, member.getId(), false)
                )
            );
        }
        return URI.create(
            RESERVATION_URI_PREFIX + reservationService.create(member, new ReservationRequest(scheduleId))
        );
    }
}
