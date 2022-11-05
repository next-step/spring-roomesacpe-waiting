package roomescape.reservation;

import roomescape.schedule.Schedule;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;
    private String status;

    public ReservationResponse(Long id, Schedule schedule, String status) {
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

    public String getStatus() {
        return status;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule(),
                reservation.getStatus().name());
    }
}
