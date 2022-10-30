package nextstep.member;

import auth.AuthPrincipal;
import auth.LoginMember;
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
    public ResponseEntity me(@LoginMember AuthPrincipal authPrincipal) {
        if (authPrincipal.isAnonymous()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(memberService.findById(Long.parseLong(authPrincipal.getPrincipal())));
    }
}
