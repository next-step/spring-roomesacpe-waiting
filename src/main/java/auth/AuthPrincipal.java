package auth;

public class AuthPrincipal {

    private final String principal;

    public AuthPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipal() {
        return principal;
    }

    public boolean isAnonymous() {
        return principal == null;
    }
}
