package nextstep.account;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class AccountDao {

    public final JdbcTemplate jdbcTemplate;

    public AccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Account> rowMapper = (resultSet, rowNum) -> new Account(
            resultSet.getLong("sales.id"),
            resultSet.getLong("sales.reservation_id"),
            resultSet.getInt("sales.tx_amount")
    );

    public Long save(Account account) {
        String sql = "INSERT INTO sales (reservation_id, tx_amount) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, account.getReservationId());
            ps.setLong(2, account.getTxAmount());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
