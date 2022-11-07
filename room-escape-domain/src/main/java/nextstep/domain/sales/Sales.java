package nextstep.domain.sales;

import nextstep.domain.Identity;
import nextstep.domain.reservation.Reservation;

public class Sales {
    private Identity id;
    private Long amount;
    private SalesStatus salesStatus;
    private Reservation reservation;

    public Sales(Identity id, Long amount, SalesStatus salesStatus, Reservation reservation) {
        this.id = id;
        this.amount = amount;
        this.salesStatus = salesStatus;
        this.reservation = reservation;
    }

    public Identity getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
