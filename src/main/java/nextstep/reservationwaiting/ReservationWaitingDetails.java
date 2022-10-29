package nextstep.reservationwaiting;

public class ReservationWaitingDetails {

    private final Long id;
    private final ScheduleDetails schedule;
    private final Long waitNum;

    public ReservationWaitingDetails(Long id, ScheduleDetails schedule, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public Long getId() {
        return id;
    }

    public ScheduleDetails getSchedule() {
        return schedule;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
