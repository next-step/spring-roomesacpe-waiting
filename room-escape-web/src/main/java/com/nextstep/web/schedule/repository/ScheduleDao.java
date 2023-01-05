package com.nextstep.web.schedule.repository;

import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.theme.repository.ThemeDao;
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
import java.util.Optional;

@Repository
public class ScheduleDao {
    private static final String TABLE_NAME = "SCHEDULE";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ScheduleRowMapper rowMapper;
    private final ThemeDao themeDao;

    public ScheduleDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource, ThemeDao themeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeDao = themeDao;
        this.rowMapper = new ScheduleRowMapper();
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public Long save(ScheduleEntity scheduleEntity) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("THEME_ID", scheduleEntity.getThemeEntity().getId());
        parameters.put("DATE", scheduleEntity.getDate());
        parameters.put("TIME", scheduleEntity.getTime());

        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public List<ScheduleEntity> findAllBy(String date) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE date = :date";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("date", date);
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public List<ScheduleEntity> findAllBy(Long themeId, String date) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE theme_id = :themeId AND date = :date";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date);
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public void delete(Long id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(query, namedParameters);
    }

    public Optional<ScheduleEntity> findById(long scheduleId) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = :scheduleId";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("scheduleId", scheduleId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, namedParameters, rowMapper));
    }

    public class ScheduleRowMapper implements RowMapper<ScheduleEntity> {
        @Override
        public ScheduleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ScheduleEntity(
                    rs.getLong("id"),
                    themeDao.findById(rs.getLong("theme_id"))
                    .orElseThrow(() -> new BusinessException("")),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time").toLocalTime()
            );
        }
    }
}
