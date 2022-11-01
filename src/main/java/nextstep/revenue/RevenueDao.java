package nextstep.revenue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class RevenueDao {

    public final JdbcTemplate jdbcTemplate;

    public RevenueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<DailyRevenue> findDailyRevenueAt(LocalDate at) {
        String sql = "select * from daily_revenue where daily_at = ?;";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                long id = rs.getLong("id");
                long memberId = rs.getLong("member_id");
                long profit = rs.getLong("profit");
                LocalDate dailyAt = rs.getObject("daily_at", LocalDate.class);
                return new DailyRevenue(id, memberId, profit, dailyAt);
            }, at));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long save(DailyRevenue dailyRevenue) {
        String sql = "INSERT INTO daily_revenue (member_id, profit, daily_at) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, dailyRevenue.getMemberId());
            ps.setLong(2, dailyRevenue.getProfit());
            ps.setObject(3, dailyRevenue.getDailyAt());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long saveHistory(RevenueHistory revenueHistory) {
        String sql = "INSERT INTO revenue_history (daily_revenue_id, original_profit, target_profit, remark, created_at) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, revenueHistory.getDailyRevenueId());
            ps.setLong(2, revenueHistory.getOriginalProfit());
            ps.setLong(3, revenueHistory.getTargetProfit());
            ps.setString(4, revenueHistory.getRemark());
            ps.setObject(5, revenueHistory.getCreatedAt());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateProfit(DailyRevenue revenue) {
        String sql = "UPDATE daily_revenue set profit = ? where id = ?;";
        jdbcTemplate.update(sql, revenue.getProfit(), revenue.getId());
    }
}
