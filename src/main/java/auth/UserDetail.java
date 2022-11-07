package auth;

import nextstep.member.MemberRole;

public interface UserDetail {
    Long getId();

    String getUsername();

    MemberRole getRole();

    boolean checkWrongPassword(String password);

    boolean isAdmin();
}
