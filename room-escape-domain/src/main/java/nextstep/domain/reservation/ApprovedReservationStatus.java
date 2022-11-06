package nextstep.domain.reservation;

public class ApprovedReservationStatus implements ReservationStatus{
    private static final String STATUS = "APPROVED";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel() {
        return new CancelledWaitingReservationStatus();
    }

    @Override
    public ReservationStatus approve() {
        return this;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
