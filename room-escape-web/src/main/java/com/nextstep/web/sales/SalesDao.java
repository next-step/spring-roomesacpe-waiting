package com.nextstep.web.sales;

import com.nextstep.web.reservation.repository.ReservationDao;
import nextstep.common.BusinessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SalesDao {
    private static final String TABLE_NAME = "SALES";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SalesRowMapper rowMapper;
    private final ReservationDao reservationDao;

    public SalesDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource, ReservationDao reservationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationDao = reservationDao;
        this.rowMapper = new SalesRowMapper();
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
    }

    public SalesEntity save(SalesEntity salesEntity) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("AMOUNT", salesEntity.getAmount());
        parameters.put("status", salesEntity.getStatus());
        Long id = (long) jdbcInsert.execute(parameters);
        salesEntity.setId(id);
        return salesEntity;
    }

    public class SalesRowMapper implements RowMapper<SalesEntity> {
        @Override
        public SalesEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SalesEntity(
                    rs.getLong("id"),
                    rs.getLong("amount"),
                    rs.getString("time"),
                    reservationDao.findById(rs.getLong("reservation_id"))
                    .orElseThrow(() -> new BusinessException("예약이없습니다."))
            );
        }
    }
}
