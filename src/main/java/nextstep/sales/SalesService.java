package nextstep.sales;

import nextstep.support.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class SalesService {

    private final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public Long createApproveSale(Long reservationId, int amount) {
        return salesDao.save(new Sale(reservationId, amount, SalesStatus.APPROVE));
    }

    public void cancelSale(Long reservationId) {
        Sale sale = salesDao.findById(reservationId);
        if (Objects.isNull(sale)) {
            throw new NotFoundException();
        }
        sale.cancel();
        salesDao.update(sale);
    }
}
