package nextstep.account;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao {
  public final JdbcTemplate jdbcTemplate;

  public AccountDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<AccountMoney> rowMapper = (resultSet, rowNum) -> new AccountMoney(
      resultSet.getLong("id"),
      resultSet.getLong("member_id"),
      resultSet.getLong("reservation_id"),
      resultSet.getLong("amount")
  );

  public Long save(AccountMoney money) {
    String sql = "INSERT INTO member (member_id, reservation_id, amount) VALUES (?, ?, ?);";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setLong(1, money.getMemberId());
      ps.setLong(2, money.getReservationId());
      ps.setLong(3, money.getAmount());
      return ps;

    }, keyHolder);

    return keyHolder.getKey().longValue();
  }

  public AccountMoney findByReservationIdAndMemberId(Long reservationId, Long memberId){
    String sql = """
        SELECT id, member_id, reservation_id, amount
        FROM account
        WHERE reservation_id = ? and member_id = ?
        """;
    return jdbcTemplate.queryForObject(sql, rowMapper, reservationId, memberId);
  }
}
