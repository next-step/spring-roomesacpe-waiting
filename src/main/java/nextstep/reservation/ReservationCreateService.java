package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationCreateService {

    private final MemberDao memberDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;

    public ReservationCreateService(
        MemberDao memberDao,
        ScheduleDao scheduleDao,
        ReservationDao reservationDao
    ) {
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
    }

    public Long create(Long memberId, Long scheduleId) {
        Member member = memberDao.findById(memberId);
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
            schedule,
            member
        );

        return reservationDao.save(newReservation);
    }
}
