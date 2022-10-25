package com.nextstep.web.auth;

public class UserDetail {
    private Long id;

    private UserDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static UserDetail from(String id) {
        return new UserDetail(Long.parseLong(id));
    }
}
