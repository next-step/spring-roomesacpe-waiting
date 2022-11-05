package nextstep.reservation;

public enum ReservationStatus {
    WAIT_PAYMENT("입금 대기"),
    APPROVED("예약 승인"),
    WITHDRAW("예약 철회"),
    CANCEL("예약 취소"),
    WAIT_ADMIN_CANCEL("관리자 예약 취소 대기");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
