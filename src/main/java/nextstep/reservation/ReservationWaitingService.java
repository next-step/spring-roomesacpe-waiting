package nextstep.reservation;

import static java.time.LocalDateTime.now;

import auth.AuthenticationException;
import java.util.List;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleService;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {

  public final ReservationService reservationService;
  public final ScheduleService scheduleService;
  public final ReservationWaitingDao reservationWaitingDao;

  public ReservationWaitingService(ReservationService reservationService,
      ScheduleService scheduleService, ReservationWaitingDao reservationWaitingDao) {
    this.reservationService = reservationService;
    this.scheduleService = scheduleService;
    this.reservationWaitingDao = reservationWaitingDao;
  }

  public Long create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
    if (member == null) {
      throw new AuthenticationException();
    }
    Long scheduleId = reservationWaitingRequest.getScheduleId();
    Schedule schedule = scheduleService.findById(scheduleId);
    List<Reservation> reservations = reservationService.findByScheduleId(scheduleId);

    if (canCreate(reservations)) {
      return reservationService.create(member, reservationWaitingRequest.toReservationRequest());
    }

    ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, WaitingEventType.CREATED,
        now());

    var reservationWaitings = findAllByScheduleIdAndMemberId(member, scheduleId);
    if (reservationWaitings.isExistWaiting(scheduleId)) {
      throw new DuplicateEntityException();
    }

    return reservationWaitingDao.save(reservationWaiting);
  }

  private static boolean canCreate(List<Reservation> reservations) {
    boolean allWithdrawn = reservations.stream()
        .allMatch(Reservation::isWithdrawn);
    return reservations.isEmpty() || allWithdrawn;
  }

  public ReservationWaitings findAllByScheduleIdAndMemberId(Member member, Long scheduleId) {
    return new ReservationWaitings(
        reservationWaitingDao.findByScheduleId(scheduleId), member);
  }

  public ReservationWaitings findAllByMemberId(Member member) {
    return new ReservationWaitings(reservationWaitingDao.findAll(), member);
  }

  public void completeReservation(Member member, Long scheduleId) {
    var reservationWaitings = findAllByScheduleIdAndMemberId(member, scheduleId);
    reservationWaitings.findFirstWait()
        .ifPresent(
            reservationWaiting ->
                reservationWaitingDao.save(
                    new ReservationWaiting(
                        reservationWaiting.getSchedule(),
                        reservationWaiting.getMember(),
                        WaitingEventType.COMPLETED,
                        now())
                )
        );
  }

  public void deleteById(Member member, Long scheduleId) {
    if (member == null) {
      throw new AuthenticationException();
    }
    Schedule schedule = scheduleService.findById(scheduleId);
    ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, WaitingEventType.CANCELED,
        now());

    var reservationWaitings = findAllByScheduleIdAndMemberId(member, scheduleId);
    if (!reservationWaitings.isExistWaiting(scheduleId)) {
      throw new DuplicateEntityException();
    }

    reservationWaitingDao.save(reservationWaiting);
  }
}
