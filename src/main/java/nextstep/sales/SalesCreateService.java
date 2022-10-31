package nextstep.sales;

import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SalesCreateService {

    private final SalesDao salesDao;
    private final ReservationDao reservationDao;

    public SalesCreateService(SalesDao salesDao, ReservationDao reservationDao) {
        this.salesDao = salesDao;
        this.reservationDao = reservationDao;
    }

    public void createByReservationApprove(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        Sales sales = new Sales(reservation.getThemePrice(), reservation.getId());
        salesDao.save(sales);
    }
}
