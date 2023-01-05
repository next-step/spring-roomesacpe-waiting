package nextstep.domain.member;

import nextstep.common.BusinessException;
import nextstep.domain.Identity;

import java.util.List;
import java.util.Objects;

public class Member {
    private final Identity id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final List<Role> roles;

    public Member(Identity id, String username, String password, String name, String phone, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.roles = roles;
    }

    public Long getId() {
        return id.getNumber();
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

    public List<Role> getRoles() { return roles; }

    public void validatePassword(String password) {
        if (!this.password.equals(password)) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean isAdmin() {
        return roles.stream()
                .anyMatch(role -> role == Role.ADMIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id.getNumber(), member.id.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getNumber());
    }
}
