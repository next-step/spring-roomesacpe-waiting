package com.nextstep.web.member.dto;

import com.nextstep.web.member.repository.entity.MemberEntity;
import lombok.Getter;

@Getter
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public MemberRequest(String username, String password, String name, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }
    public MemberEntity toEntity() {
        return new MemberEntity(null, username, password, name, phone, role);
    }
}
