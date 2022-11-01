package roomescape.member;

import auth.LoginMember;
import auth.LoginMemberPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Member> me(@LoginMemberPrincipal LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(member);
    }
}
