package nextstep.reservation;

import java.util.Objects;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Reservation {

  private Long id;
  private Schedule schedule;
  private Member member;
  private ReservationStatus status;

  public Reservation() {
  }

  public Reservation(Schedule schedule, Member member) {
    this(null, schedule, member, ReservationStatus.CREATED);
  }

  public Reservation(Long id, Schedule schedule, Member member, ReservationStatus reservationStatus) {
    this.id = id;
    this.schedule = schedule;
    this.member = member;
    this.status = reservationStatus;
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
    status = ReservationStatus.APPROVED;
  }

  public void withdraw() {
    status = ReservationStatus.WITHDRAWN;
  }

  public boolean isApproved() {
    return status == ReservationStatus.APPROVED;
  }

  public boolean isCreated() {
    return status == ReservationStatus.CREATED;
  }

  public boolean isWithdrawn() {
    return status == ReservationStatus.WITHDRAWN;
  }
}
