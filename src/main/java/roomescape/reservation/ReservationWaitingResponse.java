package roomescape.reservation;

import roomescape.schedule.Schedule;

public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private int waitNum;
    private boolean canceled;

    public ReservationWaitingResponse(Long id, Schedule schedule, int waitNum, boolean canceled) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
        this.canceled = canceled;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getSchedule(),
                reservationWaiting.getWaitNum(),
                reservationWaiting.isCanceled());
    }
}
