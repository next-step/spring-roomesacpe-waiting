package nextstep.domain.reservation;

import nextstep.domain.member.Member;

public interface ReservationStatus {
    ReservationStatus cancel(Member member);
    ReservationStatus approve();
    boolean isCancelled();
    String status();
}
