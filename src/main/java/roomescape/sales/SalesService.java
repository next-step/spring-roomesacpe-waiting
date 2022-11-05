package roomescape.sales;

import org.springframework.stereotype.Service;

@Service
public class SalesService {
    private final SalesDao salesDao;

    public SalesService(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    public void create(int amount) {
        salesDao.save(new Sales(amount));
    }

    public void refund(int amount) {
        salesDao.save(new Sales(-amount));
    }
}
