package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Status status;

    public enum Status {
        WAIT, // 입금 대기 (예약 미승인)
        CONFIRMED, // 예약 승인
        CANCEL, // 예약 취소 (WAIT -> 취소할 때)
        WITHDRAW, // 예약 철회 (CONFIRMED -> 취소할 때)
        WAIT_CANCEL, // 예약 취소를 위해 관리자 승인을 기다릴 때
    }

    public void approve() {
        if (this.status != Status.WAIT) {
            throw new IllegalArgumentException();
        }
        this.status = Status.CONFIRMED;
    }

    public void cancel(String role) {
        if ("MEMBER".equals(role)) {
            if (this.status == Status.WAIT) {
                this.status = Status.CANCEL;
            }

            if (this.status == Status.CONFIRMED) {
                this.status = Status.WAIT_CANCEL;
            }
            return;
        }

        if ("ADMIN".equals(role)) {
            this.status = Status.CANCEL;
            return;
        }

        throw new IllegalArgumentException();
    }

    public void approveCancel() {
        if (this.status != Status.WAIT_CANCEL) {
            throw new IllegalArgumentException();
        }
        this.status = Status.CANCEL;
    }

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.status = Status.WAIT;
    }

    public Reservation(Long id, Schedule schedule, Member member, Status status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public Status getStatus() {
        return status;
    }

    public boolean sameMember(Long memberId) {
        return memberId != null && Objects.equals(this.member.getId(), memberId);
    }
}
