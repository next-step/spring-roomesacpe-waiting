package nextstep.domain.reservation;

public class CancelledWaitingReservationStatus implements ReservationStatus{
    private static final String STATUS = "CANCELLED_WAITING";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel() {
        return new CancelledReservationStatus();
    }

    @Override
    public ReservationStatus approve() {
        return new ApprovedReservationStatus();
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
