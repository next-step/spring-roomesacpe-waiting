package nextstep.reservation.dto;

import nextstep.schedule.Schedule;

public class MyWaitingResponse {

    private final Long id;
    private final Schedule schedule;
    private final int waitNum;

    public MyWaitingResponse(Long id, Schedule schedule, int waitNum) {
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

    public int getWaitNum() {
        return waitNum;
    }
}
