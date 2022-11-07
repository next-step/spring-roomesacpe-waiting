package com.nextstep.web.member.controller;

import com.nextstep.web.auth.LoginMemberPrincipal;
import com.nextstep.web.auth.UserDetail;
import com.nextstep.web.member.LoginMember;
import com.nextstep.web.member.dto.MemberRequest;
import com.nextstep.web.member.service.MemberService;
import nextstep.domain.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity me(@LoginMemberPrincipal UserDetail userDetail) {
        Member member = memberService.read(LoginMember.from(userDetail));
        return ResponseEntity.ok(member);
    }
}
