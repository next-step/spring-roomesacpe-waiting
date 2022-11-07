package nextstep.reservation;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReservationCancellationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationCancellationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
        resultSet.getLong("reservation_cancellation.id"),
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

    @Transactional
    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation_cancellation (id, schedule_id, member_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getId());
            ps.setLong(2, reservation.getSchedule().getId());
            ps.setLong(3, reservation.getMember().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String sql = "SELECT " +
            "reservation_cancellation.id, reservation_cancellation.schedule_id, reservation_cancellation.member_id, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation_cancellation " +
            "inner join schedule on reservation_cancellation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation_cancellation.member_id = member.id " +
            "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
