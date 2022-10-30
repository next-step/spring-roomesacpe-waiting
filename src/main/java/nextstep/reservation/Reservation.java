package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private boolean canceled;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member, false);
    }

    public Reservation(Long id, Schedule schedule, Member member, boolean canceled) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.canceled = canceled;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void canceled() {
        this.canceled = true;
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

    public boolean isCanceled() {
        return canceled;
    }
}
