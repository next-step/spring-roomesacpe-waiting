package nextstep.reservation_waiting;

import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {

    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao) {
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long createReservationWaiting(Long scheduleId, Long memberId) {
        ReservationWaiting reservationWaiting = new ReservationWaiting(scheduleId, memberId);
        return reservationWaitingDao.save(reservationWaiting);
    }
}
