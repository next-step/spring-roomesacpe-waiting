package nextstep.reservation;

public enum ReservationStatus {
    REQUESTED("예약 미승인"),
    APPROVED("예약 승인"),
    CANCELED_REQUESTED("예약 취소 미승인"),
    CANCELED_APPROVED("예약 취소 승인"),
    WITHDRAW("예약 철회");

    ReservationStatus(String description) {
    }

    public void cancel() {

    }
}
