package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;

    private LocalDate requestDate;
    private LocalTime requestTime;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, LocalDate requestDate, LocalTime requestTime) {
        this(null, schedule, member, requestDate, requestTime);
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, LocalDate requestDate, LocalTime requestTime) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.requestDate = requestDate;
        this.requestTime = requestTime;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public LocalTime getRequestTime() {
        return requestTime;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationWaiting that = (ReservationWaiting) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
