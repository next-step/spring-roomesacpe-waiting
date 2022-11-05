package nextstep.sales;

public class Sales {

    private Long id;
    private Long reservationId;
    private int amount;
    private SalesStatus status;

    public Sales(Long reservationId, int amount, SalesStatus status) {
        this(null, reservationId, amount, status);
    }

    public Sales(Long id, Long reservationId, int amount, SalesStatus status) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public int getAmount() {
        return amount;
    }

    public SalesStatus getStatus() {
        return status;
    }
}
