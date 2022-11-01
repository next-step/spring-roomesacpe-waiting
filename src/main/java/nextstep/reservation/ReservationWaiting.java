package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {

    private Long id;
    private Schedule schedule;
    private Member member;
    private int seq;

    public ReservationWaiting(Long id, Schedule schedule, Member member, int seq) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.seq = seq;
    }

    public ReservationWaiting(Schedule schedule, Member member, int seq) {
        this.schedule = schedule;
        this.member = member;
        this.seq = seq;
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

    public int getSeq() {
        return seq;
    }

    public boolean sameMember(Long memberId) {
        return memberId != null && Objects.equals(this.member.getId(), memberId);
    }
}
