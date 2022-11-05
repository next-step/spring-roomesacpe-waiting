package nextstep.reservationwaiting;

import static nextstep.reservationwaiting.ReservationWaitingStatus.WAITING;

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
            ReservationWaitingStatus.valueOf(resultSet.getString("reservation_waiting.status"))
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, status) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getScheduleId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setString(3, reservationWaiting.getStatusName());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateStatus(ReservationWaiting reservationWaiting) {
        String sql = "UPDATE reservation_waiting SET status = ? WHERE id = ?;";
        jdbcTemplate.update(sql, reservationWaiting.getStatusName(), reservationWaiting.getId());
    }

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT * FROM reservation_waiting WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public ReservationWaiting findFirstWaitingReservationWaitingByScheduleId(Long scheduleId) {
        String sql = "SELECT * FROM reservation_waiting WHERE schedule_id = ? AND status = ? ORDER BY id ASC LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, scheduleId, WAITING.name());
        } catch (Exception e) {
            return null;
        }
    }
}
