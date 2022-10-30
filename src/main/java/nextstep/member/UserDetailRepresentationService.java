package nextstep.member;

import auth.UserDetail;
import auth.UserRepresentationService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailRepresentationService implements UserRepresentationService {

    private final MemberDao memberDao;

    public UserDetailRepresentationService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetail findByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        return member.toUserDetail();
    }

    @Override
    public UserDetail findById(Long userId) {
        Member member = memberDao.findById(userId);
        return member.toUserDetail();
    }
}
