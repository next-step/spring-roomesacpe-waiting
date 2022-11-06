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
    ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, WaitingEventType.CREATED,
        now());

    var reservationWaitings = findAllByScheduleIdAndMemberId(scheduleId, member.getId());
    if (reservationWaitings.isExistWaiting(scheduleId)) {
      throw new DuplicateEntityException();
    }

    return reservationWaitingDao.save(reservationWaiting);
  }

  public ReservationWaitings findAllByScheduleIdAndMemberId(Long scheduleId, Long memberId) {
    return new ReservationWaitings(reservationWaitingDao.findByScheduleId(scheduleId, memberId));
  }

  public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {

    return null;
  }

  public void deleteById(Member member, Long scheduleId) {
    if (member == null) {
      throw new AuthenticationException();
    }
    Schedule schedule = scheduleService.findById(scheduleId);
    ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, WaitingEventType.CANCELED,
        now());

    var reservationWaitings = findAllByScheduleIdAndMemberId(scheduleId, member.getId());
    if (!reservationWaitings.isExistWaiting(scheduleId)) {
      throw new DuplicateEntityException();
    }

    reservationWaitingDao.save(reservationWaiting);
  }
}
