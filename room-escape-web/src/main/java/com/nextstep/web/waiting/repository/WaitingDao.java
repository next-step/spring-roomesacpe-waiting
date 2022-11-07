package com.nextstep.web.waiting.repository;

import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.schedule.repository.ScheduleDao;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.waiting.repository.entitiy.WaitingEntity;
import nextstep.common.BusinessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WaitingDao {
    private static final String TABLE_NAME = "Waiting";
    private final SimpleJdbcInsert jdbcInsert;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final WaitingRowMapper rowMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WaitingDao(ScheduleDao scheduleDao, MemberDao memberDao, NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new WaitingRowMapper();
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
    }

    public Long save(WaitingEntity waitingEntity) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("scheduleId", waitingEntity.getScheduleEntity().getId());
        parameters.put("memberId", waitingEntity.getMemberEntity().getId());
        parameters.put("waitingNumber", waitingEntity.getWaitingNumber());
        parameters.put("waitingStatus", waitingEntity.getWaitingStatus());
        parameters.put("reservationWaitingTime", waitingEntity.getReservationWaitingTime());

        return (long) jdbcInsert.execute(parameters);
    }

    public List<WaitingEntity> findByMember(MemberEntity memberEntity) {
        String query = "SELECT * FROM WAITING WHERE memberId = :memberId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("memberId", memberEntity.getId());
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public List<WaitingEntity> findBySchedule(ScheduleEntity scheduleEntity) {
        String query = "SELECT * FROM WAITING WHERE scheduleId = :scheduleId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("scheduleId", scheduleEntity.getId());
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public void delete(WaitingEntity waitingEntity) {
        String query = "DELETE FROM WAITING WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", waitingEntity.getId());
        jdbcTemplate.update(query, namedParameters);
    }

    public class WaitingRowMapper implements RowMapper<WaitingEntity> {
        @Override
        public WaitingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new WaitingEntity(
                    rs.getLong("id"),
                    scheduleDao.findById(rs.getLong("scheduleId"))
                            .orElseThrow(() -> new BusinessException("")),
                    memberDao.findById(rs.getLong("memberId"))
                            .orElseThrow(() -> new BusinessException("")),
                    rs.getInt("waitingNumber"),
                    rs.getString("waitingStatus"),
                    rs.getTimestamp("reservationWaitingTime").toLocalDateTime()
            );
        }
    }
}
