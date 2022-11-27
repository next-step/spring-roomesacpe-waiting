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
    if(isApproved()){
      throw new IllegalStateException("이미 승인 처리가 된 예약입니다");
    }
    status = ReservationStatus.APPROVED;
  }

  public void withdraw() {
    if(isWithdrawn()){
      throw new IllegalStateException("이미 취소 처리가 된 예약입니다");
    }
    status = ReservationStatus.WITHDRAWN;
  }

  public void challenge() {
    if(isChallenge()){
      throw new IllegalStateException("이미 취소 요청 처리가 된 예약입니다");
    }
    status = ReservationStatus.CHALLENGE;
  }

  private boolean isChallenge() {
    return status == ReservationStatus.CHALLENGE;
  }

  public boolean isApproved() {
    return status == ReservationStatus.APPROVED;
  }

  private boolean isCreated() {
    return status == ReservationStatus.CREATED;
  }

  public boolean isWithdrawn() {
    return status == ReservationStatus.WITHDRAWN;
  }
}
