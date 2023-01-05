package nextstep.domain.reservation;

import nextstep.common.BusinessException;
import nextstep.domain.member.Member;

public class CancelledWaitingReservationStatus implements ReservationStatus{
    private static final String STATUS = "CANCELLED_WAITING";

    @Override
    public String status() {
        return STATUS;
    }

    @Override
    public ReservationStatus cancel(Member member) {
        if (!member.isAdmin()) {
            throw new BusinessException("관리자만 취소승인 가능합니다.");
        }
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
