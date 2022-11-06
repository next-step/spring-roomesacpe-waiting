package nextstep.reservation;

import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {

    public Long id;
    public Schedule schedule;
    public Member member;
    public Integer waitNum;

    public ReservationWaiting(Schedule schedule, Member member, Integer waitNum) {
        this(null, schedule, member, waitNum);
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, Integer waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public Integer getWaitNum() {
        return waitNum;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
