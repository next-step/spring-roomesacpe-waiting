package roomescape.reservation;

public enum ReservationStatus {
    STANDBY("입금대기"),
    APPROVED("승인"),
    CANCEL_REQUESTED("취소요청"),
    CANCELED("취소");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
