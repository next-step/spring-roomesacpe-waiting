package nextstep.sales;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class SalesDao {

    private final JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sales> rowMapper = (resultSet, rowNum) -> new Sales(
        resultSet.getLong("id"),
        resultSet.getInt("price"),
        SalesStatus.valueOf(resultSet.getString("status")),
        resultSet.getLong("reservation_id")
    );

    public Long save(Sales sales) {
        String sql = "INSERT INTO sales (price, status, reservation_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, sales.getPrice());
            ps.setString(2, sales.getStatusName());
            ps.setLong(3, sales.getReservationId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
