package com.nextstep.web.sales;

import nextstep.domain.sales.Sales;
import nextstep.domain.sales.SalesRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSalesRepository implements SalesRepository {
    private final SalesDao salesDao;

    public JdbcSalesRepository(SalesDao salesDao) {
        this.salesDao = salesDao;
    }

    @Override
    public Sales save(Sales sales) {
        return salesDao.save(SalesEntity.of(sales)).fromThis();
    }
}
