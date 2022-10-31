package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDao implements UserDetailsRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserDetails> rowMapper = (resultSet, rowNum) -> new UserDetails(
        resultSet.getLong("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        resultSet.getString("role")
    );

    @Override
    public UserDetails findMemberById(Long id) {
        String sql = "SELECT id, username, password, role from member where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public UserDetails findAdminById(Long id) {
        String sql = "SELECT id, username, password, role from member where id = ? and role = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id, "ADMIN");
    }

    @Override
    public UserDetails findByUsername(String username) {
        String sql = "SELECT id, username, password, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }
}
