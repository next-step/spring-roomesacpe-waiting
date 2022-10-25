package com.nextstep.web.member;

import com.nextstep.web.auth.UserDetail;

public class LoginMember {
    private Long id;

    private LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static LoginMember from(UserDetail userDetail) {
        return new LoginMember((userDetail.getId()));
    }
}
