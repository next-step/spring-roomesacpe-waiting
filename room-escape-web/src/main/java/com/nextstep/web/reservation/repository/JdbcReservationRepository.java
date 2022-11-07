package com.nextstep.web.reservation.repository;

import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import com.nextstep.web.schedule.repository.ScheduleDao;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.usecase.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public JdbcReservationRepository(ReservationDao reservationDao, ScheduleDao scheduleDao) {
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Long save(Reservation reservation) {
        return reservationDao.save(ReservationEntity.of(reservation));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationDao.findById(id).map(ReservationEntity::fromThis);
    }

    @Override
    public Optional<Reservation> findByScheduleId(Long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId).map(ReservationEntity::fromThis);
    }

    @Override
    public List<Reservation> findByMemberName(String name) {
        return reservationDao.findByMemberName(name).stream()
                .map(ReservationEntity::fromThis)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAllBy(String date) {
        List<Long> scheduleIds = scheduleDao.findAllBy(date).stream()
                .map(ScheduleEntity::getId)
                .collect(Collectors.toList());

        return reservationDao.findAllBy(scheduleIds).stream()
                .map(ReservationEntity::fromThis)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        reservationDao.delete(id);
    }

    @Override
    public void update(Reservation reservation) {
        reservationDao.update(ReservationEntity.of(reservation));
    }

}
