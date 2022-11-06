package com.nextstep.web.reservation.repository.entity;

import nextstep.domain.reservation.*;

public enum ReservationStatusEntity {
    BEFORE_DEPOSITED(new BeforeDepositedReservationStatus()),
    APPROVED(new ApprovedReservationStatus()),
    CANCELLED_WAITING(new CancelledWaitingReservationStatus()),
    CANCELLED(new CancelledReservationStatus());

    private ReservationStatus reservationStatus;

    private ReservationStatusEntity(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }
}
