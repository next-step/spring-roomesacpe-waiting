package nextstep.waiting;

import auth.AuthenticationException;
import auth.UserDetail;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class WaitingService {

    private final WaitingDao waitingDao;
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public WaitingService(
            WaitingDao waitingDao,
            ScheduleDao scheduleDao,
            ReservationDao reservationDao,
            MemberDao memberDao
    ) {
        this.waitingDao = waitingDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public WaitingCreationResponse create(UserDetail userDetail, WaitingRequest waitingRequest) {
        Schedule schedule = scheduleDao.findById(waitingRequest.getScheduleId());
        Member member = memberDao.findByUsername(userDetail.getUsername());

        checkUserAlreadyReserved(schedule, member);
        checkDuplicatedWaiting(schedule, member);

        boolean waitingExists = waitingDao.existsBySchedule(schedule.getId());
        if (isEmptySchedule(schedule) && waitingExists) {
            Long reservationId = reservationDao.save(new Reservation(schedule, member));
            return WaitingCreationResponse.reserved(reservationId);
        }
        Long waitingId = waitingDao.save(Waiting.nextOf(schedule, member, findLastOnSchedule(schedule)));
        return WaitingCreationResponse.waiting(waitingId);
    }

    public List<WaitingResponse> findByUser(UserDetail userDetail) {
        Member member = memberDao.findByUsername(userDetail.getUsername());
        return waitingDao.findByMemberOrderByScheduleId(member.getId()).stream()
                .map(it -> new WaitingResponse(
                        it.getId(),
                        scheduleDao.findById(it.getScheduleId()),
                        waitingDao.countSequenceIsLessThan(it.getScheduleId(), it.getSequenceNumber()))
                ).collect(Collectors.toList());
    }

    public void delete(UserDetail userDetail, Long id) {
        Member member = memberDao.findByUsername(userDetail.getUsername());
        Waiting waiting = waitingDao.findById(id).orElseThrow();
        if (!waiting.isOwner(member)) {
            throw new AuthenticationException();
        }
        waitingDao.deleteById(id);
    }

    private void checkUserAlreadyReserved(Schedule schedule, Member member) {
        List<Reservation> reservationsByUserOnSchedule = reservationDao.findAllAliveByScheduleIdAndMemberId(schedule.getId(), member.getId());
        if (!reservationsByUserOnSchedule.isEmpty()) {
            throw new IllegalArgumentException("이미 해당 고객에게 예약된 일정입니다.");
        }
    }

    private void checkDuplicatedWaiting(Schedule schedule, Member member) {
        List<Waiting> waitingByUserOnSchedule = waitingDao.findByScheduleIdAndMemberId(schedule.getId(),
                member.getId());
        if (!waitingByUserOnSchedule.isEmpty()) {
            throw new IllegalArgumentException("이미 해당 고객에게 예약 대기가 존재하는 일정입니다.");
        }
    }

    private Optional<Waiting> findLastOnSchedule(Schedule schedule) {
        List<Waiting> waitingList = waitingDao.findByScheduleOrderBySeq(schedule.getId());
        if(waitingList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(waitingList.get(waitingList.size() - 1));
    }

    private boolean isEmptySchedule(Schedule schedule) {
        return reservationDao.findAllAliveByScheduleId(schedule.getId()).isEmpty();
    }
}
