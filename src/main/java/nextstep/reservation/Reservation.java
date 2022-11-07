package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;
import org.apache.coyote.http11.filters.VoidOutputFilter;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private boolean canceled;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.canceled = false;
        this.status = ReservationStatus.NOT_APPROVED;
    }

    public Reservation(Long id, Schedule schedule, Member member, boolean canceled, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.canceled = canceled;
        this.status = status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public boolean isApproved() {
        return status.equals(ReservationStatus.APPROVED);
    }

    public boolean isNotApproved() {
        return status.equals(ReservationStatus.NOT_APPROVED);
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

    public boolean getCanceled() {
        return canceled;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
