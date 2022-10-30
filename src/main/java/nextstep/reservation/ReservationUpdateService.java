package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationUpdateService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationUpdateService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public void canceledById(Long memberId, Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null) {
            throw new NullPointerException();
        }
        Member member = memberDao.findById(memberId);
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
        reservation.canceled();
        reservationDao.updateCanceled(reservation);
    }
}
