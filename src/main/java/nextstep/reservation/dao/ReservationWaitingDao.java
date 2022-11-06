package nextstep.reservation.dao;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import nextstep.member.Member;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
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

    private final RowMapper<ReservationWaiting> reservationWaiting = (resultSet, rowNum) -> new ReservationWaiting(
        resultSet.getLong("reservation_waiting.id"),
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
        new Member(
            resultSet.getLong("member.id"),
            resultSet.getString("member.username"),
            resultSet.getString("member.password"),
            resultSet.getString("member.name"),
            resultSet.getString("member.phone"),
            resultSet.getString("member.role")
        ),
        resultSet.getInt("reservation_waiting.wait_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, wait_num) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setInt(2, reservationWaiting.getWaitNum());

            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT "
            + "reservation_waiting.id, "
            + "schedule.id, schedule.theme_id, schedule.date, schedule.time, "
            + "member.id, member.username, member.password, member.name, member.phone, member.role "
            + "reservation_waiting.wait_num"
            + "FROM reservation_waiting"
            + "WHERE schedule.id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, reservationWaiting, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationWaiting> findByScheduleId(Long id) {
        String sql = "SELECT "
            + "reservation_waiting.id, "
            + "schedule.id, schedule.theme_id, schedule.date, schedule.time, "
            + "member.id, member.username, member.password, member.name, member.phone, member.role "
            + "reservation_waiting.wait_num"
            + "FROM reservation_waiting"
            + "WHERE schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, reservationWaiting, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
