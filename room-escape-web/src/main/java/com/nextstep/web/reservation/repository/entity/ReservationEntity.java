package com.nextstep.web.reservation.repository.entity;

import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.reservation.Reservation;

import java.time.LocalDateTime;

@Getter
public class ReservationEntity {
    private Long id;
    private ScheduleEntity scheduleEntity;
    private LocalDateTime reservationTime;
    private ReservationStatusEntity reservationStatusEntity;
    private MemberEntity memberEntity;

    public ReservationEntity(Long id, ScheduleEntity scheduleEntity, LocalDateTime reservationTime, ReservationStatusEntity reservationStatusEntity, MemberEntity memberEntity) {
        this.id = id;
        this.scheduleEntity = scheduleEntity;
        this.reservationTime = reservationTime;
        this.reservationStatusEntity = reservationStatusEntity;
        this.memberEntity = memberEntity;
    }

    public Reservation fromThis() {
        return new Reservation(new Identity(id), scheduleEntity.fromThis(), reservationTime, reservationStatusEntity.getReservationStatus(),
                memberEntity.fromThis());
    }

    public static ReservationEntity of(Reservation reservation) {
        return new ReservationEntity(reservation.getIdNumber(), ScheduleEntity.of(reservation.getSchedule()), reservation.getReservationTime(),
                ReservationStatusEntity.valueOf(reservation.getReservationStatus().status()), MemberEntity.of(reservation.getMember()));
    }
}

