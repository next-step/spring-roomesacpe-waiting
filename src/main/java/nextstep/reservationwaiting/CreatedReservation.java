package nextstep.reservationwaiting;

public class CreatedReservation {

    private final Long id;
    private final ReservationType type;

    public CreatedReservation(Long id, ReservationType type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public ReservationType getType() {
        return type;
    }
}
