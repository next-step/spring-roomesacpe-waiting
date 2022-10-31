package nextstep.reservation;

import static nextstep.reservation.ReservationStatus.*;

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

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member, WAIT_PAYMENT);
    }

    public Reservation(Long id, Schedule schedule, Member member, ReservationStatus status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void approve() {
        this.status = APPROVED;
    }

    public void canceled() {
        // TODO: 2022/10/31 do something
    }

    public int getThemePrice() {
        return schedule.getThemePrice();
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

    public String getStatusName() {
        return status.name();
    }

    public String getStatusDescription() {
        return status.getDescription();
    }
}
