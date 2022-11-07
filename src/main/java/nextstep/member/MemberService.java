package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    @Override
    public UserDetails findByUsername(String username) {
        return memberDao.findByUsername(username);
    }

    @Override
    public Member findById(Long id) {
        return memberDao.findById(id);
    }
}
