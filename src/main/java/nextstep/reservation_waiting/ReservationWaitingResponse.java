package nextstep.reservation_waiting;

import nextstep.schedule.ScheduleResponse;

public class ReservationWaitingResponse {
    private Long id;
    private ScheduleResponse scheduleResponse;
    private Long waitNum;

    public ReservationWaitingResponse(Long id, ScheduleResponse scheduleResponse, Long waitNum) {
        this.id = id;
        this.scheduleResponse = scheduleResponse;
        this.waitNum = waitNum;
    }

    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting, Long waitNum) {
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                ScheduleResponse.from(reservationWaiting.getSchedule()),
                waitNum
        );
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getScheduleResponse() {
        return scheduleResponse;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
