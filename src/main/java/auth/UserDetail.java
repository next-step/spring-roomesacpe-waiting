package auth;

public interface UserDetail {
    Long getId();

    String getUsername();

    String getRole();

    boolean checkWrongPassword(String password);
}
