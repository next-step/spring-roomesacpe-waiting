package nextstep.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class SalesDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Sale> rowMapper = ((rs, rowNum) -> {
        return new Sale(
                rs.getLong("id"),
                rs.getLong("reservation_id"),
                rs.getInt("amount"),
                SalesStatus.valueOf(rs.getString("status"))
        );
    });

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Sale sale) {
        String sql = "INSERT INTO sales(reservation_id, amount, status) VALUES(?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sale.getReservationId());
            ps.setLong(2, sale.getAmount());
            ps.setString(3, sale.getStatus().name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Sale findById(Long reservationId) {
        String sql = "SELECT * FROM sales WHERE reservation_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, reservationId);
        } catch (Exception e) {
            return null;
        }
    }

    public void update(Sale sale) {
        String sql = "UPDATE sales SET amount = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, sale.getAmount(), sale.getStatus().name(), sale.getId());
    }
}
