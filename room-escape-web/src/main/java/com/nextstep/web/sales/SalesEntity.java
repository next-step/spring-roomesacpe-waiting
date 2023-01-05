package com.nextstep.web.sales;

import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import lombok.Getter;
import lombok.Setter;
import nextstep.domain.Identity;
import nextstep.domain.sales.Sales;
import nextstep.domain.sales.SalesStatus;

@Getter
@Setter
public class SalesEntity {
    private Long id;
    private Long amount;
    private String status;
    private ReservationEntity reservationEntity;

    public SalesEntity(Long id, Long amount, String status, ReservationEntity reservationEntity) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.reservationEntity = reservationEntity;
    }

    public static SalesEntity of(Sales sales) {
        return new SalesEntity(sales.getId().getNumber(), sales.getAmount(), sales.getSalesStatus().name(), ReservationEntity.of(sales.getReservation()));
    }

    public Sales fromThis() {
        return new Sales(new Identity(this.id), this.amount, SalesStatus.valueOf(this.status), this.reservationEntity.fromThis());
    }
}
