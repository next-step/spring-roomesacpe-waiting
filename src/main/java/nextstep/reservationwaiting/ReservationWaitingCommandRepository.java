package nextstep.reservationwaiting;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingCommandRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingCommandRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
        resultSet.getLong("id"),
        resultSet.getLong("schedule_id"),
        resultSet.getLong("member_id"),
        resultSet.getBoolean("hide")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, hide) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getScheduleId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setBoolean(3, reservationWaiting.isHide());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateHide(Long id, boolean hide) {
        String sql = "UPDATE reservation_waiting SET hide = ? WHERE id = ?;";
        jdbcTemplate.update(sql, hide, id);
    }
}
