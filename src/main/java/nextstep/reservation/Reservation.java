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
        this.schedule = schedule;
        this.member = member;
        this.status = status;
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

    public void approved() {
        this.status = ReservationStatus.APPROVED;
    }

    public void cancelRequested() {
        this.status = ReservationStatus.CANCELED_REQUESTED;
    }

    public void cancelApproved() {
        this.status = ReservationStatus.CANCELED_APPROVED;
    }

    public void withDraw() {
        this.status = ReservationStatus.WITHDRAW;
    }

    public boolean isCancelAble(Member member) {
        if (member.isAdmin()) {
            return true;
        } else {
            return Objects.equals(this.member.getId(), member.getId());
        }
    }

    public boolean isApproved() {
        return this.status == ReservationStatus.APPROVED;
    }

    public boolean isRequested() {
        return this.status == ReservationStatus.REQUESTED;
    }

    public boolean isCancelRequested() {
        return this.status == ReservationStatus.CANCELED_REQUESTED;
    }

    public boolean isCancelApproved() {
        return this.status == ReservationStatus.CANCELED_APPROVED;
    }

    public Integer getDeposit() {
        return this.getSchedule().getTheme().getPrice();
    }
}