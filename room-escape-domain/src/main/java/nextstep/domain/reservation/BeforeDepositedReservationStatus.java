package nextstep.domain.reservation;

import nextstep.domain.member.Member;

public class BeforeDepositedReservationStatus implements ReservationStatus {
    private static final String STATUS = "BEFORE_DEPOSITED";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel(Member member) {
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
