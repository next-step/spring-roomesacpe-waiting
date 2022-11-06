package nextstep.reservationwaiting;

import java.sql.PreparedStatement;
import java.util.List;
import nextstep.member.Member;
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

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
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
            "reservation_waiting.canceled, reservation_waiting.created_at, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation_waiting.member_id = member.id " +
            "where reservation_waiting.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationWaiting> findAllByScheduleId(Long scheduleId) {
        String sql = "SELECT " +
            "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
            "reservation_waiting.canceled, reservation_waiting.created_at, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation_waiting.member_id = member.id " +
            "where reservation_waiting.schedule_id = ?;";
        return jdbcTemplate.query(sql, rowMapper, scheduleId);
    }

    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        String sql = "SELECT " +
            "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
            "reservation_waiting.canceled, reservation_waiting.created_at, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation_waiting.member_id = member.id " +
            "where reservation_waiting.member_id = ?;";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public void updateCanceledById(boolean canceled, Long id) {
        String sql = "UPDATE reservation_waiting SET canceled = ? where id = ?;";
        jdbcTemplate.update(sql, canceled, id);
    }
}
