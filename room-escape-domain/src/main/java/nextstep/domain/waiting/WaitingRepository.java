package nextstep.domain.waiting;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.util.List;

public interface WaitingRepository {
    Waiting save(Waiting waiting);
    List<Waiting> findByMember(Member member);
    List<Waiting> findBySchedule(Schedule schedule);
    void delete(Waiting waiting);
}
