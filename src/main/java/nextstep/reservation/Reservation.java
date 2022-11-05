package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, ReservationStatus status) {
        this(null, schedule, member, status);
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
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

    public ReservationStatus getStatus() {
        return status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void approve() {
        this.status = ReservationStatus.APPROVE;
    }

    public void cancelRequest() {
        this.status = ReservationStatus.CANCEL_REQUEST;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCEL;
    }

    public boolean isPaymentWaiting() {
        return ReservationStatus.PAYMENT_WAITING.equals(this.status);
    }

    public boolean isApprove() {
        return ReservationStatus.APPROVE.equals(this.status);
    }
}
