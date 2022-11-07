package nextstep.account;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void saveSales(Long reservationId, Integer price) {
        Account newAccount = new Account(reservationId, price);
        accountDao.save(newAccount);
    }

    public void saveRefund(Long reservationId, Integer price) {
        Account newAccount = new Account(reservationId, -price);
        accountDao.save(newAccount);
    }
}
