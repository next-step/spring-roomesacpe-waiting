package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
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
            ),
        Reservation.Status.valueOf(resultSet.getString("reservation.status"))
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
        resultSet.getInt("waiting.seq")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, status) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            ps.setString(3, reservation.getStatus().toString());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long saveWaiting(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO waiting (schedule_id, member_id, seq) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setInt(3, reservationWaiting.getSeq());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, " +
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

    public Reservation findById(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, " +
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

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public void deleteWaitingById(Long id) {
        String sql = "DELETE FROM waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public int findLastWaitingSeq(Long scheduleId) {
        String sql = "SELECT max(seq) from WAITING where schedule_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId);
        } catch (Exception e) {
            return 0;
        }
    }

    public ReservationWaiting findWaitingById(Long id) {
        String sql = "SELECT " +
            "waiting.id, waiting.seq, waiting.schedule_id, waiting.member_id, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from waiting " +
            "inner join schedule on waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on waiting.member_id = member.id " +
            "where waiting.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, waitingRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String sql = "SELECT " +
            "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation " +
            "inner join schedule on reservation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation.member_id = member.id " +
            "where reservation.member_id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<ReservationWaiting> findWaitingsByMemberId(Long memberId) {
        String sql = "SELECT " +
            "waiting.id, waiting.seq, waiting.schedule_id, waiting.member_id, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from waiting " +
            "inner join schedule on waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on waiting.member_id = member.id " +
            "where waiting.member_id = ?;";

        try {
            return jdbcTemplate.query(sql, waitingRowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void updateStatus(Reservation reservation) {
        String sql = "UPDATE reservation set status = ? where id = ?;";
        jdbcTemplate.update(sql, reservation.getStatus().name(), reservation.getId());
    }
}
