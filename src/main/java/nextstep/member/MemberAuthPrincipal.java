package nextstep.member;

import auth.AuthPrincipal;
import auth.AuthenticationException;
import auth.TokenRequest;
import auth.UserDetail;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthPrincipal implements AuthPrincipal {

    private final MemberDao memberDao;

    public MemberAuthPrincipal(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void checkTokenRequest(TokenRequest request) {
        Member member = memberDao.findByUsername(request.getUsername());
        if (member == null || member.checkWrongPassword(request.getPassword())) {
            throw new AuthenticationException();
        }
    }

    @Override
    public UserDetail getUserDetail(String username) {
        Member member = memberDao.findByUsername(username);
        return new UserDetail(member.getUsername(), member.getRole());
    }
}
