package auth;

public class UserDetail {

    private String username;
    private String role;

    public UserDetail() {
    }

    public UserDetail(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
