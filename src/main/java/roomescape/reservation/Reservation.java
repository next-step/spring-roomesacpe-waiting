package roomescape.reservation;

import roomescape.member.Member;
import roomescape.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member, ReservationStatus.STANDBY);
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
    public int getPrice() {
        return schedule.getTheme().getPrice();
    }

    public boolean isCreatedBy(Member member) {
        return Objects.equals(this.member.getId(), member.getId());
    }
}
