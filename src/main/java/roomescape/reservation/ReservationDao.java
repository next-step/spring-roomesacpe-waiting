package roomescape.reservation;

import roomescape.member.Member;
import roomescape.schedule.Schedule;
import roomescape.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
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
            )
    );

    private final RowMapper<ReservationWaiting> waitingRowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("waiting.id"),
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
            resultSet.getInt("waiting.wait_num"),
            resultSet.getBoolean("waiting.canceled")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Reservation findById(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ?;";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsByScheduleId(Long scheduleId) {
        String sql = "SELECT COUNT(*) FROM reservation where schedule_id = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId) > 0;
    }

    public Long saveWaiting(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO waiting (schedule_id, member_id, wait_num, canceled) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setInt(3, reservationWaiting.getWaitNum());
            ps.setBoolean(4, reservationWaiting.isCanceled());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int findMaxWaitNumByScheduleId(Long scheduleId) {
        String sql = "SELECT MAX(wait_num) FROM waiting where schedule_id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId);
        } catch (Exception e) {
            return 0;
        }
    }

    public ReservationWaiting findWaitingById(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, waiting.wait_num, waiting.canceled, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where waiting.id = ?;";

        return jdbcTemplate.queryForObject(sql, waitingRowMapper, id);
    }

    public List<ReservationWaiting> findAllWaitingsByMemberId(Long memberId) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, waiting.wait_num, waiting.canceled, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, waitingRowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void cancelWaitingById(Long id) {
        String sql = "UPDATE waiting SET canceled = ? WHERE id = ?;";
        jdbcTemplate.update(sql, true, id);
    }
}
