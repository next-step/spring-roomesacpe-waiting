package com.nextstep.web.reservation.app;

import com.nextstep.web.reservation.dto.ReservationResponse;
import nextstep.domain.reservation.usecase.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationQueryService {
    private final ReservationRepository reservationRepository;

    public ReservationQueryService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> findAllBy(String date) {
        return ReservationResponse.toListFrom(reservationRepository.findAllBy(date));
    }
}
