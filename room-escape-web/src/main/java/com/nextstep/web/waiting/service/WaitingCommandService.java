package com.nextstep.web.waiting.service;

import com.nextstep.web.member.LoginMember;
import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.waiting.dto.WaitingRequest;
import nextstep.common.BusinessException;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import nextstep.domain.waiting.Waiting;
import nextstep.domain.waiting.WaitingRepository;
import nextstep.domain.waiting.Waitings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WaitingCommandService {
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final WaitingRepository waitingRepository;
    private final MemberDao memberDao;

    public WaitingCommandService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, WaitingRepository waitingRepository, MemberDao memberDao) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.waitingRepository = waitingRepository;
        this.memberDao = memberDao;
    }

    public String create(WaitingRequest waitingRequest, LoginMember loginMember) {
        MemberEntity memberEntity = memberDao.findById(loginMember.getId())
                .orElseThrow(() -> new RuntimeException("유저가없어요"));

        Schedule schedule = scheduleRepository.findById(waitingRequest.getScheduleId())
                .orElseThrow(() -> new RuntimeException("스케쥴이없어요"));

        Waitings waitings = new Waitings(waitingRepository.findBySchedule(schedule));

        return reservationRepository.findByScheduleId(waitingRequest.getScheduleId())
                .map(reservation -> "/reservation-waiting/" +
                        waitingRepository.save(new Waiting(null, schedule, memberEntity.fromThis(), waitings.nextWaitingNumber()))
                        .getId().getNumber())
                .orElse("reservation/" + reservationRepository.save(new Reservation(null, schedule, memberEntity.fromThis())));
    }

    public void delete(Long id, LoginMember loginMember) {
        MemberEntity memberEntity = memberDao.findById(loginMember.getId())
                .orElseThrow(() -> new RuntimeException("유저가없어요"));
        Waiting waiting = waitingRepository.findByMember(memberEntity.fromThis()).stream()
                .filter(w -> w.getId().getNumber().equals(id))
                .findAny()
                .orElseThrow(() -> new BusinessException("해당 예약대기가 걸려있지 않습니다."));

        waitingRepository.delete(waiting);
    }

    public void moveUp(Schedule schedule) {
        Waitings waitings = new Waitings(waitingRepository.findBySchedule(schedule));
        waitings.moveUp();
    }
}
