package com.nextstep.web.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReservationRequest {

    private Long scheduleId;

    public CreateReservationRequest(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
