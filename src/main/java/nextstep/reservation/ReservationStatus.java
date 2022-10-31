package nextstep.reservation;

public enum ReservationStatus {
    WAIT_PAYMENT("입금 대기"),
    ACCEPTED("예약 승인"),
    NOT_ACCEPTED("예약 미승인"),
    WITHDRAW("예약 철회");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
