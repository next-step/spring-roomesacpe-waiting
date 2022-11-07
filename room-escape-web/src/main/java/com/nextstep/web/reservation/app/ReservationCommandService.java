package com.nextstep.web.reservation.app;

import com.nextstep.web.member.LoginMember;
import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.reservation.dto.CreateReservationRequest;
import com.nextstep.web.waiting.service.WaitingCommandService;
import nextstep.common.AuthException;
import nextstep.common.BusinessException;
import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.exception.DuplicationReservationException;
import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.sales.Sales;
import nextstep.domain.sales.SalesRepository;
import nextstep.domain.sales.SalesStatus;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final SalesRepository salesRepository;
    private final WaitingCommandService waitingCommandService;
    private final MemberDao memberDao;;

    public ReservationCommandService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, SalesRepository salesRepository, WaitingCommandService waitingCommandService, MemberDao memberDao) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.salesRepository = salesRepository;
        this.waitingCommandService = waitingCommandService;
        this.memberDao = memberDao;
    }

    public Long save(CreateReservationRequest request, LoginMember loginMember) {
        reservationRepository.findByScheduleId(request.getScheduleId()).ifPresent((reservation -> {
            throw new DuplicationReservationException();
        }));

        Member member = validateMember(loginMember);
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() ->
                new BusinessException(""));


        Reservation reservation = new Reservation(null, schedule, member);
        return reservationRepository.save(reservation);
    }

    public void approve(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.update(reservation.approve());
        Long amount = reservation.getSchedule().getTheme().getPrice();
        salesRepository.save(new Sales(null, amount, SalesStatus.REVENUE, reservation));
    }

    public void delete(Long id, LoginMember loginMember) {
        Member member = validateMember(loginMember);
        Reservation reservation = getReservation(id);
        validateReservationMember(reservation, member);
        reservationRepository.delete(id);
    }

    public void cancel(Long id, LoginMember loginMember) {
        Member member = validateMember(loginMember);
        Reservation reservation = getReservation(id);
        validateReservationMember(reservation, member);

        Reservation cancelledReservation = reservation.cancel(member);
        if (cancelledReservation.isCancelled()) {
            waitingCommandService.moveUp(reservation.getSchedule());
        }
    }

    public void approveCancel(Long id, LoginMember loginMember) {
        Member member = validateMember(loginMember);
        Reservation reservation = getReservation(id);
        Reservation cancelledReservation = reservation.cancel(member);
        reservationRepository.update(cancelledReservation);

        Long amount = reservation.getSchedule().getTheme().getPrice();
        salesRepository.save(new Sales(null, amount, SalesStatus.REFUND, reservation));
    }

    private void validateReservationMember(Reservation reservation, Member member) {
        if (!reservation.isReservationBy(member)) {
            throw new AuthException();
        }
    }

    private Member validateMember(LoginMember loginMember) {
        MemberEntity memberEntity = memberDao.findById(loginMember.getId()).orElseThrow(() ->
                new BusinessException(""));

        return memberEntity.fromThis();
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 예약이 없습니다."));
    }
}
