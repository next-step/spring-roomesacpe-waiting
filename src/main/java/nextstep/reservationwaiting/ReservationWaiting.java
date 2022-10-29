package nextstep.reservationwaiting;

import java.util.Objects;

public class ReservationWaiting {

    private Long id;
    private Long scheduleId;
    private Long memberId;
    private boolean hide;

    protected ReservationWaiting() {
    }

    public ReservationWaiting(Long scheduleId, Long memberId) {
        this(null, scheduleId, memberId, false);
    }

    public ReservationWaiting(Long id, Long scheduleId, Long memberId, boolean hide) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.hide = hide;
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

    public boolean isHide() {
        return hide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationWaiting that = (ReservationWaiting) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
