package roomescape.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class SalesDao {
    private final JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sales> rowMapper = (resultSet, rowNum) -> new Sales(
            resultSet.getLong("id"),
            resultSet.getInt("amount")
    );

    public Long save(Sales sales) {
        String sql = "INSERT INTO sales (amount) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sales.getAmount());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
