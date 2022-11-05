package nextstep.reservation;

import java.util.Arrays;

public enum ReservationStatus {
    PAYMENT_WAITING("입금 대기"), APPROVE("예약 승인");

    private final String desc;

    ReservationStatus(String desc) {
        this.desc = desc;
    }

    public static ReservationStatus from(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 상태입니다. name: " + name));
    }

    public String getDesc() {
        return desc;
    }
}
