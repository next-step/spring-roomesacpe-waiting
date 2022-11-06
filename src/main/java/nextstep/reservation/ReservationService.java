package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetail;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

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

    public Long create(UserDetail userDetail, ReservationRequest reservationRequest) {
        Member member = getMemberFromUserDetail(userDetail);
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findAllAliveByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllAliveByThemeIdAndDate(themeId, date);
    }

    public void deleteById(UserDetail userDetail, Long id) {
        Member member = getMemberFromUserDetail(userDetail);
        Reservation reservation = reservationDao.findAliveById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMember(UserDetail userDetail) {
        Member member = getMemberFromUserDetail(userDetail);
        return ReservationResponse.listOf(reservationDao.findByMemberId(member.getId()));
    }

    private Member getMemberFromUserDetail(UserDetail userDetail) {
        if (userDetail == null) {
            throw new AuthenticationException();
        }

        return memberDao.findByUsername(userDetail.getUsername());
    }
}
