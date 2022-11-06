package nextstep.reservation.dto;

import nextstep.schedule.Schedule;

public class ReservationWaitingResponse {

    private Long id;
    private Schedule schedule;
    private Integer waitNum;

    private ReservationWaitingResponse() {
    }

    public ReservationWaitingResponse(Long id, Schedule schedule, Integer waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Integer getWaitNum() {
        return waitNum;
    }
}
