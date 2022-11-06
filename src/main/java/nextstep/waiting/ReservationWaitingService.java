package nextstep.waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationStatus;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ReservationDao reservationDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationDao reservationDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public Long create(Member member, ReservationWaitingRequest request) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(request.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        // 이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다.
        if (reservationDao.existsByScheduleId(schedule.getId())) {
            return reservationWaitingDao.save(
                    new ReservationWaiting(schedule,
                            member,
                            LocalDate.now(),
                            LocalTime.now()
                    )
            );

        } else {
            // 예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다.
            Reservation newReservation = new Reservation(
                    schedule,
                    member,
                    ReservationStatus.REQUESTED
            );
            return reservationDao.save(newReservation);

        }
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        if (member == null) {
            throw new AuthenticationException();
        }
        ReservationWaiting waiting = reservationWaitingDao.findById(id);
        if (waiting == null) {
            throw new NullPointerException();
        }

        if (!waiting.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationWaitingResponse> findMine(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        List<ReservationWaiting> waitings = reservationWaitingDao.findByMemberIdOrderByDateTimeAsc(member.getId());

        return ReservationWaitingResponse.fromList(waitings);
    }
}
