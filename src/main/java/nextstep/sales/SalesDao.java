package nextstep.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class SalesDao {

    public final JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sales> rowMapper = (resultSet, rowNum) -> new Sales(
            resultSet.getLong("sales.id"),
            resultSet.getLong("sales.reservation_id"),
            resultSet.getInt("sales.tx_amount")
    );

    public Long save(Sales sales) {
        String sql = "INSERT INTO sales (reservation_id, tx_amount) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sales.getReservationId());
            ps.setLong(2, sales.getTxAmount());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
