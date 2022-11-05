package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
public class DataLoader implements CommandLineRunner {
    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) {
        memberDao.save(new Member("bada", "password", "bada", "010-1234-1234", "ADMIN"));
    }
}
