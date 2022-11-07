package com.nextstep.web.member.repository.entity;

import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.member.Member;
import nextstep.domain.member.Role;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class MemberEntity {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public MemberEntity(Long id, String username, String password, String name, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }


    public static MemberEntity of(Member member) {
        return new MemberEntity(member.getId(), member.getUsername(), member.getPassword(), member.getName(), member.getPhone(),
                String.join(",", member.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList())));
    }

    public Member fromThis() {
        return new Member(new Identity(id),
                username,
                password,
                name,
                phone, Arrays.asList(role.split(",")).stream()
                .map(Role::valueOf)
                .collect(Collectors.toList()));
    }
}
