package auth;

public class UserDetail {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public UserDetail() {
    }

    public UserDetail(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public UserDetail(String username, String password, String name, String phone, String role) {
        this(null, username, password, name, phone, role);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
