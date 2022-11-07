package nextstep.reservation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import org.springframework.lang.Nullable;

public record ReservationWaitings(List<ReservationWaiting> reservationWaitings, Member member) {

  public ReservationWaitings {
    if (reservationWaitings == null) {
      reservationWaitings = new ArrayList<>();
    }

    reservationWaitings = reservationWaitings.stream()
        .sorted(Comparator.comparing(ReservationWaiting::getCreatedAt))
        .collect(Collectors.toList())
    ;

    setWaitCount(reservationWaitings);
  }

  @Override
  public List<ReservationWaiting> reservationWaitings() {
    return reservationWaitings.stream()
        .filter(reservationWaiting -> reservationWaiting.sameMember(member))
        .toList();
  }

  public boolean isExistWaiting(Long scheduleId) {
    var reservationWaiting = getLastElement(scheduleId);

    if (reservationWaiting == null) {
      return false;
    }

    return reservationWaiting.getEventType() == WaitingEventType.CREATED;
  }

  public Optional<ReservationWaiting> findFirstWait() {
    return reservationWaitings.stream()
        .filter(reservationWaiting -> reservationWaiting.getWaitNumber() == 1)
        .max(Comparator.comparing(ReservationWaiting::getCreatedAt));
  }

  private void setWaitCount(List<ReservationWaiting> reservationWaitings) {
    Map<Schedule, List<ReservationWaiting>> groupBySchedule = reservationWaitings.stream()
        .collect(Collectors.groupingBy(ReservationWaiting::getSchedule));

    // 이벤트가 CREATED 인 경우 waitNumber가 필요하고, 나머지는 필요하지 않다.
    groupBySchedule.forEach((schedule, reservationWaitingList) -> {
          int count = 0;
          for (ReservationWaiting reservationWaiting : reservationWaitingList) {
            switch (reservationWaiting.getEventType()) {
              case CREATED -> reservationWaiting.setWaitNumber(++count);
              case CANCELED, COMPLETED -> count--;
            }
          }
        }
    );
  }

  @Nullable
  private ReservationWaiting getLastElement(Long scheduleId) {
    return this.reservationWaitings.stream()
        .filter(entity -> entity.sameSchedule(scheduleId))
        .reduce((first, second) -> second)
        .orElse(null);
  }
}
