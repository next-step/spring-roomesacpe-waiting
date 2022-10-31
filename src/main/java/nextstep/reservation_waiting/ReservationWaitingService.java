package nextstep.reservation_waiting;

import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationWaitingService {

    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationService reservationService;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationService reservationService) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationService = reservationService;
    }

    public Long createReservationWaiting(Long scheduleId, Member member) {
        if (!reservationService.existsReservationInSchedule(scheduleId)) {
            return reservationService.create(member, new ReservationRequest(scheduleId));
        }
        ReservationWaiting reservationWaiting = new ReservationWaiting(scheduleId, member.getId());
        return reservationWaitingDao.save(reservationWaiting);
    }

    public void deleteReservationWaiting(Long reservationWaitingId) {
        reservationWaitingDao.deleteById(reservationWaitingId);
    }
}
