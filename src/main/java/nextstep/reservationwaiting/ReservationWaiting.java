package nextstep.reservationwaiting;

import static nextstep.reservationwaiting.ReservationWaitingStatus.*;
import static nextstep.reservationwaiting.ReservationWaitingStatus.CANCELED;
import static nextstep.reservationwaiting.ReservationWaitingStatus.WAITING;

import auth.AuthenticationException;
import java.util.Objects;

public class ReservationWaiting {

    private Long id;
    private Long scheduleId;
    private Long memberId;
    private ReservationWaitingStatus status;

    protected ReservationWaiting() {
    }

    public ReservationWaiting(Long scheduleId, Long memberId) {
        this(null, scheduleId, memberId, WAITING);
    }

    public ReservationWaiting(Long id, Long scheduleId, Long memberId, ReservationWaitingStatus status) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.status = status;
    }

    public void cancelIfOwner(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new AuthenticationException();
        }
        this.status = CANCELED;
    }

    public void promote() {
        this.status = PROMOTED;
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

    public ReservationWaitingStatus getStatus() {
        return status;
    }

    public String getStatusName() {
        return status.name();
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
