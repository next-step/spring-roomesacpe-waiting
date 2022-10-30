package auth;

public interface AuthRepository {
    Auth findByUsername(String username);
}
