package nextstep.reservation;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<ReservationResponse> listOf(List<Reservation> reservations) {
        return reservations.stream()
                .map(it -> new ReservationResponse(it.getId(), it.getSchedule(), it.getCanceled()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public boolean getCanceled() {
        return canceled;
    }
}
