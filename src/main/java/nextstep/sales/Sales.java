package nextstep.sales;

public class Sales {
    private final Long id;
    private final Long reservationId;
    private final Integer txAmount;

    public Sales(Long reservationId, Integer txAmount) {
        this(null, reservationId, txAmount);
    }

    public Sales(Long id, Long reservationId, Integer txAmount) {
        this.id = id;
        this.reservationId = reservationId;
        this.txAmount = txAmount;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public int getTxAmount() {
        return txAmount;
    }
}
