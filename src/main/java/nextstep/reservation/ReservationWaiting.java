package nextstep.reservation;

import java.time.LocalDateTime;
import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private WaitingEventType eventType;
    private LocalDateTime createdAt;
    private Integer waitNumber;

    private ReservationWaiting() {
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, WaitingEventType eventType,
        LocalDateTime createdAt) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.eventType = eventType;
        this.createdAt = createdAt;
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

    public WaitingEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getWaitNumber() {
        return waitNumber;
    }

    public void setWaitNumber(int waitNumber) {
        this.waitNumber = waitNumber;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public boolean sameSchedule(Long scheduleId) {
        return schedule != null && Objects.equals(this.schedule.getId(), scheduleId);
    }
}
