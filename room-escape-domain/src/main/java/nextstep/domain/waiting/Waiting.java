package nextstep.domain.waiting;

import nextstep.domain.Identity;
import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;

public class Waiting {
    private final Identity id;
    private final Schedule schedule;
    private final Member member;
    private final int waitingNumber;
    private final WaitingStatus waitingStatus;
    private final LocalDateTime reservationWaitingTime;

    public Waiting(Identity id, Schedule schedule, Member member, WaitingStatus waitingStatus, int waitingNumber, LocalDateTime reservationWaitingTime) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitingStatus = waitingStatus;
        this.waitingNumber = waitingNumber;
        this.reservationWaitingTime = reservationWaitingTime;
    }


    public Waiting(Identity id, Schedule schedule, Member member, int waitingNumber) {
       this(id, schedule, member, WaitingStatus.WAITING, waitingNumber, LocalDateTime.now());
    }


    public Identity getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }

    public WaitingStatus getWaitingStatus() {
        return waitingStatus;
    }

    public LocalDateTime getReservationWaitingTime() {
        return reservationWaitingTime;
    }

    public void moveUp() {
    }
}
