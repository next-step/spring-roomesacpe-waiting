package nextstep.member;

import auth.Auth;
import auth.AuthRepository;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthRepository implements AuthRepository {

    private final MemberDao memberDao;

    public MemberAuthRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Auth findByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        return new Auth(member.getId().toString(), member.getUsername(), member.getPassword(), member.getRole());
    }
}
