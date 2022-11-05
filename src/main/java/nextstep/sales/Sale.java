package nextstep.sales;

public class Sale {

    private Long id;
    private Long reservationId;
    private int amount;
    private SalesStatus status;

    public Sale(Long reservationId, int amount, SalesStatus status) {
        this(null, reservationId, amount, status);
    }

    public Sale(Long id, Long reservationId, int amount, SalesStatus status) {
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

    public void cancel() {
        this.status = SalesStatus.CANCEL;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
