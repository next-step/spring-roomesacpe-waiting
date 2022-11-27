package nextstep.reservation;

import nextstep.account.AccountDao;
import nextstep.account.AccountMoney;
import nextstep.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationFacade {

  private final ReservationService reservationService;
  private final ReservationWaitingService reservationWaitingService;
  private final ReservationDao reservationDao;
  private final AccountDao accountDao;

  public ReservationFacade(ReservationService reservationService,
      ReservationWaitingService reservationWaitingService, ReservationDao reservationDao,
      AccountDao accountDao) {
    this.reservationService = reservationService;
    this.reservationWaitingService = reservationWaitingService;
    this.reservationDao = reservationDao;
    this.accountDao = accountDao;
  }

  public void deleteReservation(Member member, Long reservationId) {
    Reservation reservation = reservationDao.findById(reservationId, false);
    reservationService.deleteById(member, reservationId);
    reservationWaitingService.completeReservation(member, reservation.getSchedule().getId());
  }

  @Transactional
  public void approveReservation(Long reservationId, Long amount) {
    Reservation reservation = reservationDao.findById(reservationId, false);
    reservationService.approve(reservation);
    accountDao.save(new AccountMoney(reservation.getId(), reservation.getMember().getId(), amount));
  }

  @Transactional
  public void cancelReservation(Long reservationId) {
    Reservation reservation = reservationDao.findById(reservationId, false);
    if (reservation.isApproved()) {
      reservationService.challenge(reservation);
    } else {
      reservationService.withdraw(reservation);
      reservationWaitingService.reservationNextWaiting(reservation.getSchedule().getId());
      refundMoney(reservation);
    }
  }

  @Transactional
  public void cancelReservation(Member member, Long reservationId) {
    Reservation reservation = reservationDao.findById(reservationId, false);
    if (!reservation.sameMember(member)) {
      throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
    }
    cancelReservation(reservationId);
  }

  private void refundMoney(Reservation reservation) {
    AccountMoney accountMoney = accountDao.findByReservationIdAndMemberId(reservation.getId(),
        reservation.getMember().getId());
    AccountMoney refund = accountMoney.refund();
    accountDao.save(refund);
  }
}
