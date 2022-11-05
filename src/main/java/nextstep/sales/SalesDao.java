package nextstep.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class SalesDao {
    private final JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Sales sales) {
        String sql = "INSERT INTO sales(reservation_id, amount, status) VALUES(?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sales.getReservationId());
            ps.setLong(2, sales.getAmount());
            ps.setString(3, sales.getStatus().name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
