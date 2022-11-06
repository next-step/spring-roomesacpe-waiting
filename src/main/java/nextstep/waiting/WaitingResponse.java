package nextstep.waiting;

import java.util.List;
import nextstep.schedule.Schedule;

public class WaitingResponse {

    private Long id;
    private Schedule schedule;
    private int waitNum;

    public WaitingResponse() {
    }

    public WaitingResponse(Long id, Schedule schedule, int waitNum) {
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
