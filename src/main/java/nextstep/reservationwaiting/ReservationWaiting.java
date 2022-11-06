package nextstep.reservationwaiting;

import java.time.LocalDateTime;

public class ReservationWaiting {

    private Long id;
    private Long scheduleId;
    private Long memberId;
    private boolean canceled;
    private LocalDateTime createdAt;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Long scheduleId, Long memberId, boolean canceled) {
        this(null, scheduleId, memberId, canceled, null);
    }

    public ReservationWaiting(Long id, Long scheduleId, Long memberId, boolean canceled, LocalDateTime createdAt) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.canceled = canceled;
        this.createdAt = createdAt;
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

    public boolean isCanceled() {
        return canceled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
