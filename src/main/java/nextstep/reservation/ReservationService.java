package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetail;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.revenue.Revenue;
import nextstep.revenue.RevenueDao;
import nextstep.revenue.RevenueStatus;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import nextstep.waiting.Waiting;
import nextstep.waiting.WaitingDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final WaitingDao waitingDao;
    private final RevenueDao revenueDao;

    public ReservationService(
            ReservationDao reservationDao,
            ThemeDao themeDao,
            ScheduleDao scheduleDao,
            MemberDao memberDao,
            WaitingDao waitingDao,
            RevenueDao revenueDao
    ) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.waitingDao = waitingDao;
        this.revenueDao = revenueDao;
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

        Reservation newReservation = new Reservation(schedule, member);
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

        reservationDao.cancelById(id);
    }

    public void approve(UserDetail userDetail, Long id) {
        Member member = getMemberFromUserDetail(userDetail);
        if (member.getRole() != "ADMIN") {
            throw new AuthenticationException();
        }
        Reservation reservation = reservationDao.findAliveById(id);
        if(reservation == null) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
        if (reservation.getStatus() != ReservationStatus.NOT_APPROVED) {
            throw new IllegalArgumentException("승인될 수 없는 상태의 예약입니다.");
        }
        reservationDao.changeStatus(id, ReservationStatus.APPROVED);
        revenueDao.save(Revenue.profitOf(reservation));
    }

    public void cancel(UserDetail userDetail, Long id) {
        Member member = getMemberFromUserDetail(userDetail);
        Reservation reservation = reservationDao.findAliveById(id);

        if(reservation == null) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }

        if(reservation.isApproved() && member.isUser()) {
            reservationDao.changeStatus(reservation.getId(), ReservationStatus.CANCEL_REQUESTED);
            return;
        }

        if (reservation.isApproved() && member.isAdmin()) {
            revenueDao.changeStatusByReservation(reservation.getId(), RevenueStatus.REFUND);
        }

        reservationDao.cancelById(reservation.getId());
        reserveFirstWaitingOfSchedule(reservation);
    }

    public void cancelApprove(UserDetail userDetail, Long id) {
        Member member = getMemberFromUserDetail(userDetail);
        Reservation reservation = reservationDao.findAliveById(id);

        if(!member.isAdmin()) {
            throw new AuthenticationException();
        }

        if(!reservation.getStatus().equals(ReservationStatus.CANCEL_REQUESTED)) {
            throw new IllegalArgumentException("취소 요청 상태의 예약이 아닙니다.");
        }

        revenueDao.changeStatusByReservation(reservation.getId(), RevenueStatus.REFUND);
        reservationDao.cancelById(reservation.getId());
        reserveFirstWaitingOfSchedule(reservation);
    }

    private void reserveFirstWaitingOfSchedule(Reservation reservation) {
        Schedule schedule = reservation.getSchedule();
        List<Waiting> waitingList = waitingDao.findByScheduleOrderBySequence(schedule.getId());
        if (!waitingList.isEmpty()) {
            Waiting firstOne = waitingList.get(0);
            reservationDao.save(new Reservation(schedule, memberDao.findById(firstOne.getMemberId())));
            waitingDao.deleteById(firstOne.getId());
        }
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
