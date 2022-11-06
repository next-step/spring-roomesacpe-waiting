package nextstep.reservation;

import nextstep.schedule.Schedule;

public class ReservationWaitings {

    public Long id;
    public Schedule schedule;
    public Integer waitNum;

    public ReservationWaitings(Schedule schedule, Integer waitNum) {
        this(null, schedule, waitNum);
    }

    public ReservationWaitings(Long id, Schedule schedule, Integer waitNum) {
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
