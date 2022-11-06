package nextstep.reservation;

import java.time.LocalDateTime;
import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private final Long id;
    private final Schedule schedule;
    private final Member member;
    private final WaitingEventType eventType;
    private final LocalDateTime created_at;

    public ReservationWaiting(Long id, Schedule schedule, Member member, WaitingEventType eventType,
        LocalDateTime created_at) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.eventType = eventType;
        this.created_at = created_at;
    }

    public ReservationWaiting(Schedule schedule, Member member, WaitingEventType eventType,
        LocalDateTime created_at) {
        this(null, schedule, member, eventType, created_at);
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
