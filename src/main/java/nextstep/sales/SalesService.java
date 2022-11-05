package nextstep.sales;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesService {

    private final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public Long createApproveSales(Long reservationId, int amount) {
        return salesDao.save(new Sales(reservationId, amount, SalesStatus.APPROVE));
    }
}
