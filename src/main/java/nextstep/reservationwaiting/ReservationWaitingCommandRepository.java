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
            resultSet.getLong("reservation_waiting.id"),
            resultSet.getLong("reservation_waiting.schedule_id"),
            resultSet.getLong("reservation_waiting.member_id"),
            resultSet.getBoolean("reservation_waiting.canceled")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, canceled) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getScheduleId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setBoolean(3, reservationWaiting.isCanceled());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateCanceled(ReservationWaiting reservationWaiting) {
        String sql = "UPDATE reservation_waiting SET canceled = ? WHERE id = ?;";
        jdbcTemplate.update(sql, reservationWaiting.isCanceled(), reservationWaiting.getId());
    }

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT * FROM reservation_waiting WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
