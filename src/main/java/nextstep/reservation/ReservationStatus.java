package nextstep.reservation;

public enum ReservationStatus {
  CREATED("예약 생성"),
  APPROVED("예약 승인"),
  CHALLENGE("취소 요청"),
  WITHDRAWN("취소 완료")
  ;

  private final String description;

  ReservationStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
