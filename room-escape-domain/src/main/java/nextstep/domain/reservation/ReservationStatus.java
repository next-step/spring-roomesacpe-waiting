package nextstep.domain.reservation;

public interface ReservationStatus {
    ReservationStatus cancel();
    ReservationStatus approve();
    boolean isCancelled();
    String status();
}
