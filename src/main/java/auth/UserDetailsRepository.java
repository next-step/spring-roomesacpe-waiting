package auth;

public interface UserDetailsRepository {

    UserDetails findByUsername(String username);

    UserDetails findMemberById(Long id);
}
