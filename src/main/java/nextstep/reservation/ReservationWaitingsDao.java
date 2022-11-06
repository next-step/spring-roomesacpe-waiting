package nextstep.reservation;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingsDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaitings> reservationWaitings = (resultSet, rowNum) -> new ReservationWaitings(
        resultSet.getLong("reservation_waitings.id"),
        new Schedule(
            resultSet.getLong("schedule.id"),
            new Theme(
                resultSet.getLong("theme.id"),
                resultSet.getString("theme.name"),
                resultSet.getString("theme.desc"),
                resultSet.getInt("theme.price")
            ),
            resultSet.getDate("schedule.date").toLocalDate(),
            resultSet.getTime("schedule.time").toLocalTime()
        ),
        resultSet.getInt("reservation_waitings.wait_num")
    );

    public Long save(ReservationWaitings reservationWaitings) {
        String sql = "INSERT INTO reservation_waitings (schedule_id, wait_num) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

            ps.setLong(1, reservationWaitings.getSchedule().getId());
            ps.setInt(2, reservationWaitings.getWaitNum());

            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationWaitings> findByScheduleId(Long id) {
        String sql = "SELECT "
            + "reservation_waitings.id, "
            + "schedule.id, schedule.theme_id, schedule.date, schedule.time, "
            + "reservation_waitings.wait_num"
            + "FROM reservation_waitings"
            + "WHERE schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, reservationWaitings, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
