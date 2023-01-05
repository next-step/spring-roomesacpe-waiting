package com.nextstep.web.waiting.repository.entitiy;

import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import lombok.Getter;
import lombok.Setter;
import nextstep.domain.Identity;
import nextstep.domain.waiting.Waiting;
import nextstep.domain.waiting.WaitingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class WaitingEntity {
    private Long id;
    private ScheduleEntity scheduleEntity;
    private MemberEntity memberEntity;
    private int waitingNumber;
    private String waitingStatus;
    private LocalDateTime reservationWaitingTime;

    public WaitingEntity(Long id, ScheduleEntity scheduleEntity, MemberEntity memberEntity, int waitingNumber, String waitingStatus, LocalDateTime reservationWaitingTime) {
        this.id = id;
        this.scheduleEntity = scheduleEntity;
        this.memberEntity = memberEntity;
        this.waitingNumber = waitingNumber;
        this.waitingStatus = waitingStatus;
        this.reservationWaitingTime = reservationWaitingTime;
    }

    public static WaitingEntity of(Waiting waiting) {
        return new WaitingEntity(waiting.getId().getNumber(), ScheduleEntity.of(waiting.getSchedule()), MemberEntity.of(waiting.getMember()),
                waiting.getWaitingNumber(), waiting.getWaitingStatus().name(), waiting.getReservationWaitingTime());
    }

    public Waiting fromThis() {
        return new Waiting(new Identity(id), scheduleEntity.fromThis(), memberEntity.fromThis(),
                 WaitingStatus.valueOf(waitingStatus), waitingNumber, reservationWaitingTime);
    }
}
