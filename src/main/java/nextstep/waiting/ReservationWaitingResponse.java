package nextstep.waiting;

import nextstep.schedule.ScheduleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationWaitingResponse {
    private final Long id;
    private final ScheduleResponse schedule;
    private final Integer waitNum;

    public ReservationWaitingResponse(Long id, ScheduleResponse schedule, Integer waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public static List<ReservationWaitingResponse> fromList(List<ReservationWaiting> waitings, List<ReservationWaiting> allWaiting) {
        return waitings.stream()
                .map(waiting ->
                        new ReservationWaitingResponse(
                                waiting.getId(),
                                ScheduleResponse.of(waiting.getSchedule()),
                                allWaiting.indexOf(waiting)
                        )
                ).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public Integer getWaitNum() {
        return waitNum;
    }
}
