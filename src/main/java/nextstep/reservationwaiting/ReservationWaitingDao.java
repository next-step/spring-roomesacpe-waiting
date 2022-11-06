package nextstep.reservationwaiting;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
        resultSet.getLong("reservation_waiting.id"),
        resultSet.getLong("reservation_waiting.schedule_id"),
        resultSet.getLong("reservation_waiting.member_id"),
        resultSet.getBoolean("reservation_waiting.canceled"),
        resultSet.getTimestamp("reservation_waiting.created_at").toLocalDateTime()
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

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT " +
            "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
            "reservation_waiting.canceled, reservation_waiting.created_at " +
            "from reservation_waiting " +
            "where reservation_waiting.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateCanceledById(boolean canceled, Long id) {
        String sql = "UPDATE reservation_waiting SET canceled = ? where id = ?;";
        jdbcTemplate.update(sql, canceled, id);
    }
}
