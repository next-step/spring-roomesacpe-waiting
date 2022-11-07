package nextstep.revenue;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class RevenueDao {

    public final JdbcTemplate jdbcTemplate;

    public RevenueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Revenue revenue) {
        String sql = "INSERT INTO revenue (reservation_id, status) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, revenue.getReservationId());
            ps.setString(2, revenue.getStatus().name().toUpperCase());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void changeStatusByReservation(Long reservationId, RevenueStatus status) {
        String sql = "UPDATE revenue SET status = ? WHERE reservation_id = ?";
        jdbcTemplate.update(sql, status.name().toUpperCase(), reservationId);
    }
}
