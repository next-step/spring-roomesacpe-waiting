package nextstep.revenue;

import nextstep.reservation.Reservation;

public class Revenue {

    private Long id;
    private Long reservationId;
    private RevenueStatus status;

    public Revenue() {
    }

    public Revenue(Long reservationId, RevenueStatus status) {
        this.reservationId = reservationId;
        this.status = status;
    }

    public static Revenue profitOf(Reservation reservation) {
        return new Revenue(reservation.getId(), RevenueStatus.PROFIT);
    }

    public Revenue(Long id, Long reservationId, RevenueStatus status) {
        this.id = id;
        this.reservationId = reservationId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public RevenueStatus getStatus() {
        return status;
    }
}
