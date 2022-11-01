package roomescape.reservation;

import auth.AuthenticationException;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.schedule.Schedule;
import roomescape.schedule.ScheduleDao;
import roomescape.support.DuplicateEntityException;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = findSchedule(reservationRequest.getScheduleId());
        Member member = findMember(memberId);
        checkReservationAvailable(schedule.getId());

        Reservation newReservation = new Reservation(schedule, member);
        return reservationDao.save(newReservation);
    }

    private void checkReservationAvailable(Long scheduleId) {
        if (reservationDao.existsByScheduleId(scheduleId)) {
            throw new DuplicateEntityException("이미 예약된 스케줄입니다.");
        }
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        try {
            themeDao.findById(themeId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 테마입니다.");
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long id) {
        Reservation reservation = findReservation(id);
        Member member = findMember(memberId);
        checkMyReservation(reservation, member);

        if (reservationDao.deleteById(id) == 0) {
            throw new NullPointerException("존재하지 않는 예약입니다.");
        }
    }

    private void checkMyReservation(Reservation reservation, Member member) {
        if (!reservation.isMine(member)) {
            throw new AuthenticationException("해당 예약에 대한 권한이 없습니다");
        }
    }

    private Schedule findSchedule(Long scheduleId) {
        try {
            return scheduleDao.findById(scheduleId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 스케줄입니다.");
        }
    }

    private Member findMember(Long memberId) {
        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 멤버입니다.");
        }
    }

    private Reservation findReservation(Long reservationId) {
        try {
            return reservationDao.findById(reservationId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 예약입니다.");
        }
    }
}
