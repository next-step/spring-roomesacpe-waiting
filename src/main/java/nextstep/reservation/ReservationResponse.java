package nextstep.reservation;

public class ReservationResponse {

    private final Long id;
    private final ScheduleResponse schedule;

    public ReservationResponse(Long id, ScheduleResponse schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            ScheduleResponse.from(reservation.getSchedule())
        );
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }
}
