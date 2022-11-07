package nextstep.waiting;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class WaitingDao {
    private JdbcTemplate jdbcTemplate;

    public WaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Waiting> rowMapper = (resultSet, rowNum) -> new Waiting(
            resultSet.getLong("id"),
            resultSet.getLong("schedule_id"),
            resultSet.getLong("member_id"),
            resultSet.getInt("sequence_number")
    );

    public Long save(Waiting waiting) {
        String sql = "INSERT INTO waiting (schedule_id, member_id, sequence_number) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, waiting.getScheduleId());
            ps.setLong(2, waiting.getMemberId());
            ps.setLong(3, waiting.getSequenceNumber());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Waiting> findByScheduleIdAndMemberId(Long scheduleId, Long memberId) {
        String sql = "SELECT id, schedule_id, member_id, sequence_number " +
                "from waiting " +
                "where schedule_id = ? AND member_id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, scheduleId, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }

    }

    public List<Waiting> findByScheduleOrderBySequence(Long scheduleId) {
        String sql = "SELECT id, schedule_id, member_id, sequence_number " +
                "from waiting " +
                "where schedule_id = ?" +
                "ORDER BY sequence_number;";

        try {
            return jdbcTemplate.query(sql, rowMapper, scheduleId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Waiting> findByMemberOrderByScheduleId(Long memberId) {
        String sql = "SELECT id, schedule_id, member_id, sequence_number " +
                "from waiting " +
                "where member_id = ?" +
                "ORDER BY schedule_id;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public int countSequenceIsLessThan(Long scheduleId, int sequence) {
        String sql = "SELECT count(id) as cnt FROM waiting WHERE schedule_id = ? AND sequence_number < ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> resultSet.getInt("cnt"), scheduleId, sequence) +1;
    }

    public Optional<Waiting> findById(Long id) {
        try {
            String sql = "SELECT id, schedule_id, member_id, sequence_number " +
                    "from waiting " +
                    "where id = ?;";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException ERDA) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM waiting where id = ?;", id);
    }

    public boolean existsBySchedule(Long scheduleId) {
        String sql = "SELECT exists (select 1 FROM waiting WHERE schedule_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, scheduleId);
    }
}
