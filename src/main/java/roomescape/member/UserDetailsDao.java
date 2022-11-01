package roomescape.member;

import auth.UserDetails;
import auth.UserDetailsRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsDao implements UserDetailsRepository {
    private final MemberDao memberDao;

    public UserDetailsDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails findByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        return new UserDetails(member.getId(), member.getUsername(), member.getPassword(), member.getRole());
    }
}
