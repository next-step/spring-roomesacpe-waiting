package nextstep.domain.reservation;

import nextstep.domain.Identity;
import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Identity id;
    private final Schedule schedule;
    private final LocalDateTime reservationTime;
    private final ReservationStatus reservationStatus;
    private final Member member;

    public Reservation(Identity id, Schedule schedule, LocalDateTime reservationTime, ReservationStatus reservationStatus, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.reservationTime = reservationTime;
        this.reservationStatus = reservationStatus;
        this.member = member;
    }

    public Reservation(Identity id, Schedule schedule, Member member) {
        this(id, schedule, LocalDateTime.now(), new BeforeDepositedReservationStatus(), member);
    }

    public Identity getId() {
        return id;
    }

    public Long getIdNumber() {
        if (Objects.isNull(id)) {
            return null;
        }
        return id.getNumber();
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public Member getMember() {
        return member;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public boolean isReservationBy(Member member) {
        System.out.println(this.member.equals(member));
       return this.member.equals(member);
    }

    public Reservation approve() {
        return new Reservation(this.id, this.schedule, this.reservationTime, reservationStatus.approve(), this.member);
    }

    public Reservation cancel(Member member) {
        return new Reservation(this.id, this.schedule, this.reservationTime, reservationStatus.cancel(member), this.member);
    }

    public boolean isCancelled() {
        return reservationStatus.isCancelled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id.getNumber(), that.id.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getNumber());
    }
}
