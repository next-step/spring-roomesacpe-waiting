package nextstep.reservation;

public class ReservationResponse {

    private final Long id;
    private final ScheduleResponse schedule;
    private final String status;

    public ReservationResponse(Long id, ScheduleResponse schedule, String status) {
        this.id = id;
        this.schedule = schedule;
        this.status = status;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            ScheduleResponse.from(reservation.getSchedule()),
            reservation.getStatusDescription()
        );
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public String getStatus() {
        return status;
    }
}
