package nextstep.reservationwaiting;

import java.time.LocalDateTime;
import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationWaiting {

    private Long id;
    private Schedule schedule;
    private Member member;
    private boolean canceled;
    private LocalDateTime createdAt;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Schedule schedule, Member member, boolean canceled) {
        this(null, schedule, member, canceled, null);
    }

    public ReservationWaiting(Long id, Schedule schedule, Member member, boolean canceled, LocalDateTime createdAt) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.canceled = canceled;
        this.createdAt = createdAt;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getScheduleId() {
        return schedule.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
