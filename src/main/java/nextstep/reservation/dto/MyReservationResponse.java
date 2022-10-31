package nextstep.reservation.dto;

import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class MyReservationResponse {

    private final Long id;
    private final Schedule schedule;
    private final Reservation.Status status;

    public MyReservationResponse(Long id, Schedule schedule, Reservation.Status status) {
        this.id = id;
        this.schedule = schedule;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
