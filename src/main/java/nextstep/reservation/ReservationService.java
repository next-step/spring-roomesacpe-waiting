package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.account.AccountDao;
import nextstep.account.AccountService;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import nextstep.waiting.ReservationWaitingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ReservationWaitingService reservationWaitingService;
    public final AccountService accountService;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;

    public ReservationService(ReservationDao reservationDao, ReservationWaitingService reservationWaitingService, AccountService accountService, ThemeDao themeDao, ScheduleDao scheduleDao) {
        this.reservationDao = reservationDao;
        this.reservationWaitingService = reservationWaitingService;
        this.accountService = accountService;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
    }

    @Transactional
    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member,
                ReservationStatus.REQUESTED
        );

        return reservationDao.save(newReservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMine(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());

        return ReservationResponse.fromList(reservations);
    }

    @Transactional
    public void cancel(Member member, Long id) {
        Reservation reservation = getReservation(id);
        if (!reservation.isCancelAble(member)) {
            throw new AuthenticationException();
        }

        if (member.isAdmin()) {
            if (reservation.isRequested()) {
                withDraw(id);
            } else if (reservation.isApproved()) {
                cancelApprove(reservation.getId());
            }
        } else {
            if (reservation.isRequested()) {
                withDraw(id);
            } else if (reservation.isApproved()) {
                cancelRequested(reservation.getId());
            }
        }
    }

    private Reservation getReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        return reservation;
    }

    @Transactional
    public void approve(Long id) {
        Reservation reservation = getReservation(id);

        reservation.approved();
        reservationDao.update(reservation);

        accountService.saveSales(reservation.getId(), reservation.getDeposit());
    }

    @Transactional
    public void cancelRequested(Long id) {
        Reservation reservation = getReservation(id);
        reservation.cancelRequested();
        reservationDao.update(reservation);
    }

    @Transactional
    public void cancelApprove(Long id) {
        Reservation reservation = getReservation(id);
        reservation.cancelApproved();
        reservationDao.update(reservation);
        accountService.saveSales(reservation.getId(), reservation.getDeposit());
        withDraw(id);
    }

    @Transactional
    public void withDraw(Long id) {
        Reservation reservation = getReservation(id);
        reservation.withDraw();
        reservationDao.update(reservation);
        reservationWaitingService.changeFirstToReservation(reservation.getSchedule().getId());
    }
}
