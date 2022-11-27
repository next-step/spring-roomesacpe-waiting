package nextstep.reservation;

public enum WaitingEventType {
  CREATED("예약 대기"),
  CANCELED("예약 취소"),
  COMPLETED("예약 승인")
  ;

  private final String description;

  WaitingEventType(String description) {
    this.description = description;
  }
}
