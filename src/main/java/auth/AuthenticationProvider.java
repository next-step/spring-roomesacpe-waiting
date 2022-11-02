package auth;

public interface AuthenticationProvider {
    UserDetail findByUsername(String userName);

    UserDetail findById(Long id);
}
