package nextstep.waiting;

import nextstep.schedule.ScheduleResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ReservationWaitingResponse {
    private final Long id;
    private final ScheduleResponse schedule;
    private final Long waitNum;

    public ReservationWaitingResponse(Long id, ScheduleResponse schedule, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public static List<ReservationWaitingResponse> fromList(List<ReservationWaiting> waitings) {
        AtomicLong waitNum = new AtomicLong(1);
        return waitings.stream().map(waiting ->
                new ReservationWaitingResponse(
                        waiting.getId(),
                        ScheduleResponse.of(waiting.getSchedule()),
                        waitNum.getAndIncrement()
                )
        ).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
