package nextstep.auth;

import auth.AuthenticationException;
import auth.JwtTokenProvider;
import auth.TokenRequest;
import auth.TokenResponse;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByUsername(tokenRequest.getUsername());

        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Member extractMember(String credential) {
        if (isInvalid(credential)) {
            throw new AuthenticationException();
        }

        Long id = extractPrincipal(credential);

        return memberDao.findById(id);
    }

    private boolean isInvalid(String credential) {
        return !jwtTokenProvider.validateToken(credential);
    }

    private Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }
}
