package nextstep.reservation_waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationWaitingService {

    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationService reservationService;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationService reservationService, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationService = reservationService;
        this.scheduleDao = scheduleDao;
    }

    public Long createReservationWaiting(Long scheduleId, Member member) {
        if (!reservationService.existsReservationInSchedule(scheduleId)) {
            return reservationService.create(member, new ReservationRequest(scheduleId));
        }
        Schedule schedule = scheduleDao.findById(scheduleId);
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member.getId());
        return reservationWaitingDao.save(reservationWaiting);
    }

    public void deleteReservationWaiting(Long reservationWaitingId, Member member) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(reservationWaitingId)
                .orElseThrow(NotFoundException::new);
        if (reservationWaiting.isNotSameMember(member.getId())) {
            throw new AuthenticationException();
        }
        reservationWaitingDao.deleteById(reservationWaitingId);
    }
}
