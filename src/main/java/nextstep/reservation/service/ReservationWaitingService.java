package nextstep.reservation.service;

import auth.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationWaitingRequest;
import nextstep.reservation.dto.ReservationWaitingResponse;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReservationWaitingService {

    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationWaitingService(
        ReservationDao reservationDao,
        ScheduleDao scheduleDao,
        ReservationWaitingDao reservationWaitingDao
    ) {
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    @Transactional
    public Long putOnWaitingList(Member member, ReservationWaitingRequest request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId());
        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());

        if (reservations.isEmpty()) {
            Reservation newReservation = new Reservation(schedule, member);
            reservationDao.save(newReservation);

            return Long.MAX_VALUE;
        }

        List<ReservationWaiting> reservationWaitings = reservationWaitingDao
            .findByScheduleId(schedule.getId());

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
            schedule,
            member,
            calculateWaitNum(reservationWaitings)
        );
        return reservationWaitingDao.save(newReservationWaiting);
    }

    private int calculateWaitNum(List<ReservationWaiting> waitings) {
        return waitings.size() + 1;
    }

    @Transactional
    public void deleteFromWaitingListById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);

        if (reservationWaiting == null) {
            throw new NullPointerException();
        }
        if (!reservationWaiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }

    public List<ReservationWaitingResponse> readMyWaitingList(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId()).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private ReservationWaitingResponse toResponse(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(
            reservationWaiting.getId(),
            reservationWaiting.getSchedule(),
            reservationWaiting.getWaitNum()
        );
    }
}
