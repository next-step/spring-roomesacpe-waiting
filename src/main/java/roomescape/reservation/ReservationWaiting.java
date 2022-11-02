package roomescape.reservation;

import roomescape.member.Member;
import roomescape.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private int waitNum;

    public ReservationWaiting(Schedule schedule, Member member, int waitNum) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, int waitNum) {
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

    public int getWaitNum() {
        return waitNum;
    }

    public boolean isCreatedBy(Member member) {
        return Objects.equals(this.member.getId(), member.getId());
    }
}
