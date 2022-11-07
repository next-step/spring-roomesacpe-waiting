package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.account.AccountService;
import nextstep.member.Member;
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
public class ReservationCancelService {
    public final ReservationDao reservationDao;
    public final ReservationWaitingService reservationWaitingService;
    public final AccountService accountService;

    public ReservationCancelService(ReservationDao reservationDao, ReservationWaitingService reservationWaitingService, AccountService accountService) {
        this.reservationDao = reservationDao;
        this.reservationWaitingService = reservationWaitingService;
        this.accountService = accountService;
    }

    @Transactional
    public void cancel(Member member, Long id) {
        Reservation reservation = getReservation(id);
        if (!reservation.isCancelAble(member)) {
            throw new AuthenticationException();
        }

        if (member.isAdmin()) {
            cancelByAdmin(id, reservation);
        } else {
            cancelByMember(id, reservation);
        }
    }

    private void cancelByAdmin(Long id, Reservation reservation) {
        if (reservation.isRequested()) {
            withDraw(id);
        } else if (reservation.isApproved()) {
            cancelApprove(reservation.getId());
        }
    }

    private void cancelByMember(Long id, Reservation reservation) {
        if (reservation.isRequested()) {
            withDraw(id);
        } else if (reservation.isApproved()) {
            cancelRequested(reservation.getId());
        }
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
        accountService.saveRefund(reservation.getId(), reservation.getDeposit());
        withDraw(id);
    }

    @Transactional
    public void withDraw(Long id) {
        Reservation reservation = getReservation(id);
        reservation.withDraw();
        reservationDao.update(reservation);
        reservationWaitingService.changeFirstToReservation(reservation.getSchedule().getId());
    }

    private Reservation getReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        return reservation;
    }
}
