package nextstep.reservation.dto;

public class ReservationWaitingRequest {

    private Long scheduleId;

    public ReservationWaitingRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ReservationWaitingRequest() {
    }

    public Long getScheduleId() {
        return scheduleId;
    }
}
