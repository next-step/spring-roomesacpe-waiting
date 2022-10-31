package auth;

public class AuthPrincipal {

    private final String principal;
    private final String role;

    public AuthPrincipal(String principal, String role) {
        this.principal = principal;
        this.role = role;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getRole() {
        return role;
    }

    public boolean isAnonymous() {
        return principal == null;
    }
}
