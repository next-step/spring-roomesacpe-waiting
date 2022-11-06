package nextstep.reservation;

import auth.AuthenticationException;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationWaitingRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final ReservationWaitingDao reservationWaitingDao;

    public ReservationService(
        ReservationDao reservationDao,
        ThemeDao themeDao,
        ScheduleDao scheduleDao,
        MemberDao memberDao,
        ReservationWaitingDao reservationWaitingDao
    ) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    @Transactional
    public Long create(Member member, ReservationRequest request) {
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(request.getScheduleId());

        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());

        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(schedule, member);
        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);

        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new NullPointerException();
        }
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
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
}
