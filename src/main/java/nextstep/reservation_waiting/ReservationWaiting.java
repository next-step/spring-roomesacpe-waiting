package nextstep.reservation_waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Long memberId;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Long id, Schedule schedule, Long memberId) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
    }

    public ReservationWaiting(Schedule schedule, Long memberId) {
        this(null, schedule, memberId);
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public boolean isSameMember(Long memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isNotSameMember(Long memberId) {
        return !this.isSameMember(memberId);
    }
}
