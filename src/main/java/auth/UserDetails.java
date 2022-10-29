package auth;

public interface UserDetails {

    Long getId();

    String getUsername();

    String getPassword();

    String getRole();

    boolean checkWrongPassword(String password);
}
