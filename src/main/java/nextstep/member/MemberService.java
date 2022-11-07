package nextstep.member;

import auth.AuthenticationProvider;
import auth.UserDetail;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements AuthenticationProvider {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Override
    public UserDetail findByUsername(String userName) {
        return memberDao.findByUsername(userName);
    }

    @Override
    public UserDetail findById(Long id) {
        return memberDao.findById(id);
    }
}
