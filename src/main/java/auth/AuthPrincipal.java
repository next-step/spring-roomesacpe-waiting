package auth;

public interface AuthPrincipal {

    void checkTokenRequest(TokenRequest request);

    UserDetail getUserDetail(String username);
}
