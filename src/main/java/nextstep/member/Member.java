package nextstep.member;

import auth.UserDetail;

import java.util.Objects;

public class Member implements UserDetail {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private MemberRole role;

    public Member() {
    }

    public Member(Long id, String username, String password, String name, String phone, MemberRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public Member(String username, String password, String name, String phone, MemberRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public MemberRole getRole() {
        return role;
    }

    @Override
    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }

    @Override
    public boolean isAdmin() {
        return this.role == MemberRole.ADMIN;
    }

    public boolean checkCancelAble(Member member) {
        if (isAdmin()) {
            return true;
        } else {
            return Objects.equals(this.id, member.getId());
        }
    }
}
