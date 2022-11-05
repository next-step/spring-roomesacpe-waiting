package nextstep.reservationwaiting;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingRepresentationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingRepresentationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaitingDetails> rowMapper = (resultSet, rowNum) -> new ReservationWaitingDetails(
        resultSet.getLong("reservation_waiting.id"),
        new ScheduleDetails(
            resultSet.getLong("schedule.id"),
            new ThemeDetails(
                resultSet.getLong("theme.id"),
                resultSet.getString("theme.name"),
                resultSet.getString("theme.desc"),
                resultSet.getInt("theme.price")
            ),
            resultSet.getDate("schedule.date").toLocalDate(),
            resultSet.getTime("schedule.time").toLocalTime()
        ),
        resultSet.getLong("waitNum")
    );

    public List<ReservationWaitingDetails> findAllWaitingDetailsByMemberId(Long memberId) {
        return jdbcTemplate.query("select "
            + "reservation_waiting.id, schedule.id, theme.id, "
            + "theme.name, theme.desc, theme.price, "
            + "schedule.date, schedule.time, "
            + "(select count(*) from reservation_waiting where schedule.id = schedule.id and reservation_waiting.id <= reservation_waiting.id) as waitNum "
            + "from reservation_waiting "
            + "inner join schedule on reservation_waiting.schedule_id = schedule.id "
            + "inner join theme on schedule.theme_id = theme.id "
            + "inner join member on reservation_waiting.member_id = member.id "
            + "where member.id = ?", rowMapper, memberId);
    }
}
