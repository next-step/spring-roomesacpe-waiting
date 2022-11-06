package nextstep.reservation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;

public record ReservationWaitings(List<ReservationWaiting> reservationWaitings) {

  public ReservationWaitings {
    reservationWaitings = reservationWaitings.stream()
        .sorted(Comparator.comparing(ReservationWaiting::getCreatedAt))
        .collect(Collectors.toList())
    ;
  }

  public boolean isExistWaiting(Long scheduleId) {
    var reservationWaiting = getLastElement(scheduleId);

    if (reservationWaiting == null) {
      return false;
    }

    return reservationWaiting.getEventType() == WaitingEventType.CREATED;
  }

  public boolean isCanceledWaiting(Long scheduleId) {
    var reservationWaiting = getLastElement(scheduleId);

    if (reservationWaiting == null) {
      return false;
    }

    return reservationWaiting.getEventType() == WaitingEventType.CANCELED;
  }

  @Nullable
  private ReservationWaiting getLastElement(Long scheduleId) {
    return this.reservationWaitings.stream()
        .filter(entity -> entity.sameSchedule(scheduleId))
        .reduce((first, second) -> second)
        .orElse(null);
  }
}
