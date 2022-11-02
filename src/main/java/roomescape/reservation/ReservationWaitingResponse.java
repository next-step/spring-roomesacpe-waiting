package roomescape.reservation;

import roomescape.schedule.Schedule;

public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private int waitNum;

    public ReservationWaitingResponse(Long id, Schedule schedule, int waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
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

    public static ReservationWaitingResponse from(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(reservationWaiting.getId(), reservationWaiting.getSchedule(), reservationWaiting.getWaitNum());
    }
}
