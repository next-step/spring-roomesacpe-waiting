package nextstep.reservationwaiting;

import auth.AuthenticationException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationWaitingService {

    private static final String RESERVATION_URI_PREFIX = "/reservations/";
    private static final String RESERVATION_WAITING_URI_PREFIX = "/reservation-waitings/";

    private final ReservationService reservationService;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingService(
        ReservationService reservationService, ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao) {
        this.reservationService = reservationService;
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
    }

    @Transactional
    public URI create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        if (reservationService.existsReservation(schedule)) {
            return URI.create(
                RESERVATION_WAITING_URI_PREFIX + reservationWaitingDao.save(
                    new ReservationWaiting(schedule, member, false)
                )
            );
        }
        return URI.create(
            RESERVATION_URI_PREFIX + reservationService.create(member, new ReservationRequest(schedule.getId()))
        );
    }

    public void cancelById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting == null) {
            throw new NullPointerException();
        }
        if (!reservationWaiting.sameMember(member)) {
            throw new AuthenticationException();
        }
        reservationWaitingDao.updateCanceledById(true, id);
    }

    public List<ReservationWaitingResponse> findAllByMember(Member member) {
        List<ReservationWaiting> myWaitings = reservationWaitingDao.findAllByMemberId(member.getId());
        return myWaitings.stream()
            .filter(it -> !it.isCanceled())
            .map(
                it -> new ReservationWaitingResponse(it.getId(), it.getSchedule(), calculateWaitNum(it))
            )
            .collect(Collectors.toList());
    }

    private Long calculateWaitNum(ReservationWaiting reservationWaiting) {
        return reservationWaitingDao.findAllByScheduleId(reservationWaiting.getScheduleId())
            .stream()
            .filter(it -> !it.isCanceled())
            .filter(it -> it.getCreatedAt().isBefore(reservationWaiting.getCreatedAt()))
            .count() + 1;
    }
}
