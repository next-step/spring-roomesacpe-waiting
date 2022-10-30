package nextstep.reservation.dto;

public class ReservationWaitingResponse {

    private final Long id;
    private final boolean reserved;

    public ReservationWaitingResponse(Long id, boolean reserved) {
        this.id = id;
        this.reserved = reserved;
    }

    public Long getId() {
        return id;
    }

    public boolean isReserved() {
        return reserved;
    }
}
