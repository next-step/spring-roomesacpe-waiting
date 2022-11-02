package roomescape.reservation;

import roomescape.schedule.Schedule;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;

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

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getSchedule());
    }
}
