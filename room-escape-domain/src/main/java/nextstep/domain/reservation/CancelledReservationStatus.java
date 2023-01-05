package nextstep.domain.reservation;

import nextstep.domain.member.Member;

public class CancelledReservationStatus implements ReservationStatus{
    private static final String STATUS = "CANCELLED";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel(Member member) {
        return this;
    }

    @Override
    public ReservationStatus approve() {
        return this;
    }

    @Override
    public boolean isCancelled() {
        return true;
    }
}
