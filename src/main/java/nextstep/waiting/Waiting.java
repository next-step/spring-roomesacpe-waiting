package nextstep.waiting;

import java.util.Objects;
import java.util.Optional;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class Waiting {

    private Long id;
    private Long scheduleId;
    private Long memberId;
    private Integer sequenceNumber;

    public Waiting() {
    }

    public Waiting(Long id, Long scheduleId, Long memberId, Integer sequenceNumber) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.sequenceNumber = sequenceNumber;
    }

    public Waiting(Long scheduleId, Long memberId, Integer sequenceNumber) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.sequenceNumber = sequenceNumber;
    }

    public static Waiting nextOf(Schedule schedule, Member member, Optional<Waiting> lastOne) {
        if(lastOne.isEmpty()) {
            return new Waiting(schedule.getId(), member.getId(), 1);
        }
        int prevSequenceNumber = lastOne.orElseThrow().getSequenceNumber();
        return new Waiting(schedule.getId(), member.getId(), prevSequenceNumber +1);
    }

    public boolean isOwner(Member member) {
        return Objects.equals(memberId, member.getId());
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

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }
}
