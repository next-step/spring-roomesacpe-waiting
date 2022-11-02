package nextstep.waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationWaitingService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final ReservationWaitingDao reservationWaitingDao;

    public ReservationWaitingService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, ReservationWaitingDao reservationWaitingDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

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
            reservationWaitingDao.save(
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
                    member
            );
            reservationDao.save(newReservation);
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

}
