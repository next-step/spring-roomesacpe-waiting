package nextstep.reservation;

import nextstep.schedule.Schedule;

public class ReservationResponse {

    private Long id;
    private Schedule schedule;
    private boolean canceled;

    public ReservationResponse() {

    }

    public ReservationResponse(Long id, Schedule schedule, boolean canceled) {
        this.id = id;
        this.schedule = schedule;
        this.canceled = canceled;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
