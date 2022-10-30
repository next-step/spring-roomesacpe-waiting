package nextstep.reservation_waiting;

public class ReservationWaiting {
    private Long id;
    private Long scheduleId;
    private Long memberId;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Long id, Long scheduleId, Long memberId) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
    }

    public ReservationWaiting(Long scheduleId, Long memberId) {
        this(null, scheduleId, memberId);
    }

    public Long getId() {
        return id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
