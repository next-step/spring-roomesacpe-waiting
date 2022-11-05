package nextstep.sales;

import java.util.Objects;

public class Sales {

    private Long id;
    private int price;
    private SalesStatus status;
    private Long reservationId;

    public Sales(int price, SalesStatus status, Long reservationId) {
        this(null, price, status, reservationId);
    }

    public Sales(Long id, int price, SalesStatus status, Long reservationId) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.reservationId = reservationId;
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public SalesStatus getStatus() {
        return status;
    }

    public String getStatusName() {
        return status.name();
    }

    public Long getReservationId() {
        return reservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sales sales = (Sales) o;
        return Objects.equals(id, sales.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
