package auth;

public interface UserRepresentationService {
    UserDetail findByUsername(String username);

    UserDetail findById(Long userId);
}
