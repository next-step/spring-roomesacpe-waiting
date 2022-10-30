package nextstep.reservation.dto;

import nextstep.schedule.Schedule;

public class MyReservationResponse {

    private final Long id;
    private final Schedule schedule;

    public MyReservationResponse(Long id, Schedule schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
