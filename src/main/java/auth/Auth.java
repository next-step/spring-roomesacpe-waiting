package auth;

public class Auth {

    private String principal;
    private String username;
    private String password;
    private String role;

    public Auth(String principal, String username, String password, String role) {
        this.principal = principal;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean checkWrongPassword(String password) {
        return false;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
