package nextstep.domain.reservation;

import nextstep.domain.member.Member;

public class ApprovedReservationStatus implements ReservationStatus{
    private static final String STATUS = "APPROVED";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel(Member member) {
        if (member.isAdmin()) {
            return new CancelledReservationStatus();
        }

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
