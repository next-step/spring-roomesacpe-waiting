package com.nextstep.web.reservation.repository;

import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import com.nextstep.web.reservation.repository.entity.ReservationStatusEntity;
import com.nextstep.web.schedule.repository.ScheduleDao;
import nextstep.common.BusinessException;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ReservationDao {
    private static final String TABLE_NAME = "Reservation";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper rowMapper;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;

    public ReservationDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.rowMapper = new ReservationRowMapper();
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

    }

    public Long save(ReservationEntity reservationEntity) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("schedule_id", reservationEntity.getScheduleEntity().getId());
        parameters.put("reservation_time", reservationEntity.getReservationTime());
        parameters.put("member_id", reservationEntity.getMemberEntity().getId());
        parameters.put("status", reservationEntity.getReservationStatusEntity().name());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public List<ReservationEntity> findByMemberName(String name) {
        String query = "SELECT * FROM RESERVATION WHERE name = :name";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name);
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public List<ReservationEntity> findAllBy(List<Long> scheduleIds) {
        if (scheduleIds.isEmpty()) {
            return Collections.emptyList();
        }

        String query = "SELECT * FROM RESERVATION WHERE schedule_id IN :scheduleIds";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("scheduleIds", scheduleIds);

        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public Optional<ReservationEntity> findByScheduleId(Long scheduleId) {
        try {
            String query = "SELECT * FROM RESERVATION WHERE schedule_id = :scheduleId";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("scheduleId", scheduleId);

            return Optional.ofNullable(jdbcTemplate.queryForObject(query, namedParameters, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    public void delete(Long id) {
        String query = "DELETE FROM RESERVATION WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(query, namedParameters);
    }

    public Optional<ReservationEntity> findById(Long id) {
        try {
            String query = "SELECT * FROM RESERVATION WHERE id = :id";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("id", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, namedParameters, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(ReservationEntity reservationEntity) {
        String query = "UPDATE RESERVATION SET reservationStatus = :reservationStatus WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", reservationEntity.getId())
                .addValue("reservationStatus", reservationEntity.getReservationStatusEntity().name());
        jdbcTemplate.update(query, namedParameters);
    }

    public class ReservationRowMapper implements RowMapper<ReservationEntity> {
        @Override
        public ReservationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

            ReservationEntity reservationEntity = new ReservationEntity(
                    rs.getLong("ID"),
                    scheduleDao.findById(rs.getLong("schedule_id"))
                            .orElseThrow(() -> new BusinessException("")),
                    rs.getObject("reservation_time", LocalDateTime.class),
                    ReservationStatusEntity.valueOf(rs.getString("status")),
                    memberDao.findById(rs.getLong("member_id"))
                            .orElseThrow(() -> new BusinessException(""))
            );
            return reservationEntity;
        }
    }
}
