package nextstep.reservation;

import nextstep.member.Member;
import org.springframework.stereotype.Service;

@Service
public class ReservationFacade {

  private final ReservationService reservationService;
  private final ReservationWaitingService reservationWaitingService;
  private final ReservationDao reservationDao;

  public ReservationFacade(ReservationService reservationService,
      ReservationWaitingService reservationWaitingService, ReservationDao reservationDao) {
    this.reservationService = reservationService;
    this.reservationWaitingService = reservationWaitingService;
    this.reservationDao = reservationDao;
  }

  public void deleteReservation(Member member, Long reservationId) {
    Reservation reservation = reservationDao.findById(reservationId, false);
    reservationService.deleteById(member, reservationId);
    reservationWaitingService.completeReservation(member, reservation.getSchedule().getId());
  }
}
