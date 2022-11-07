package nextstep.reservation.dto;

import nextstep.schedule.Schedule;

public class ReservationResponse {

    private Long id;
    private Schedule schedule;

    private ReservationResponse() {
    }

    public ReservationResponse(Long id, Schedule schedule) {
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
