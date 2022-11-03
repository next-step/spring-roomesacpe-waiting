package roomescape.reservation;

import auth.AuthenticationException;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.sales.SalesService;
import roomescape.schedule.Schedule;
import roomescape.schedule.ScheduleDao;
import roomescape.support.DuplicateEntityException;
import roomescape.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;

    private final SalesService salesService;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, SalesService salesService) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.salesService = salesService;
    }

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = findSchedule(reservationRequest.getScheduleId());
        Member member = findMember(memberId);
        checkReservationAvailable(schedule.getId());

        Reservation reservation = new Reservation(schedule, member);
        return reservationDao.save(reservation);
    }

    private void checkReservationAvailable(Long scheduleId) {
        if (reservationDao.existsByScheduleId(scheduleId)) {
            throw new DuplicateEntityException("이미 예약된 스케줄입니다.");
        }
    }

    public void approveReservation(Long memberId, Long reservationId) {
        Member member = findMember(memberId);
        Reservation reservation = findReservation(reservationId);

        if (!member.isAdmin()) {
            throw new AuthenticationException("관리자만 이용가능합니다");
        }
        reservationDao.updateReservationStatusById(ReservationStatus.APPROVED.name(), reservationId);
        salesService.create(reservation.getPrice());
    }

    public Long createWaiting(Long memberId, ReservationRequest reservationRequest) {
        Schedule schedule = findSchedule(reservationRequest.getScheduleId());
        Member member = findMember(memberId);
        if (reservationDao.existsByScheduleId(schedule.getId())) {
            int waitNum = reservationDao.findMaxWaitNumByScheduleId(schedule.getId()) + 1;
            ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, waitNum);
            return reservationDao.saveWaiting(reservationWaiting);
        }
        return create(memberId, reservationRequest);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        try {
            themeDao.findById(themeId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 테마입니다.");
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<ReservationResponse> findAllByMemberId(Long memberId) {
        Member member = findMember(memberId);
        List<Reservation> reservations = reservationDao.findAllByMemberId(member.getId());
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public List<ReservationWaitingResponse> findWaitingsByMemberId(Long memberId) {
        Member member = findMember(memberId);
        List<ReservationWaiting> waitings = reservationDao.findAllWaitingsByMemberId(member.getId());
        return waitings.stream().map(ReservationWaitingResponse::from).collect(Collectors.toList());
    }

    public void cancelReservation(Long memberId, Long reservationId) {
        Member member = findMember(memberId);
        Reservation reservation = findReservation(reservationId);

        if (member.isAdmin()) {
            cancelReservationByAdmin(reservation);
        } else {
            cancelReservationByUser(reservation, member);
        }
    }

    private void cancelReservationByAdmin(Reservation reservation) {
        reservationDao.deleteById(reservation.getId());
        if (reservation.isApproved() || reservation.isCancelRequested()) {
            salesService.refund(reservation.getPrice());
            organizeWaitings(reservation.getSchedule().getId());
        }
    }

    private void organizeWaitings(Long scheduleId) {
        List<ReservationWaiting> waitings = reservationDao.findAllWaitingsByScheduleId(scheduleId);
        waitings.stream().min(Comparator.comparing(ReservationWaiting::getWaitNum))
                .ifPresent(it -> {
                    reservationDao.save(new Reservation(it.getSchedule(), it.getMember()));
                    reservationDao.deleteWaitingById(it.getId());
                });
    }

    private void cancelReservationByUser(Reservation reservation, Member member) {
        checkMyReservation(reservation, member);

        if (reservation.isApproved()) {
            reservationDao.updateReservationStatusById(ReservationStatus.CANCEL_REQUESTED.name(), reservation.getId());
        } else {
            reservationDao.deleteById(reservation.getId());
        }
    }

    public void approveCancelReservation(Long memberId, Long reservationId) {
        Member member = findMember(memberId);
        Reservation reservation = findReservation(reservationId);

        if (!member.isAdmin()) {
            throw new AuthenticationException("관리자만 이용가능합니다");
        }
        cancelReservationByAdmin(reservation);
    }

    public void deleteById(Long memberId, Long id) {
        Reservation reservation = findReservation(id);
        Member member = findMember(memberId);
        checkMyReservation(reservation, member);

        if (reservationDao.deleteById(id) == 0) {
            throw new NullPointerException("존재하지 않는 예약입니다.");
        }
    }

    public void cancelWaiting(Long memberId, Long id) {
        ReservationWaiting waiting = findReservationWaiting(id);
        Member member = findMember(memberId);
        checkMyReservationWaiting(waiting, member);

        ReservationWaiting reservationWaiting = findReservationWaiting(id);
        if (reservationWaiting.isCanceled()) {
            throw new IllegalArgumentException("이미 취소된 예약 대기입니다.");
        }
        reservationDao.cancelWaitingById(reservationWaiting.getId());
    }

    private void checkMyReservation(Reservation reservation, Member member) {
        if (!reservation.isCreatedBy(member)) {
            throw new AuthenticationException("해당 예약에 대한 권한이 없습니다");
        }
    }

    private void checkMyReservationWaiting(ReservationWaiting waiting, Member member) {
        if (!waiting.isCreatedBy(member)) {
            throw new AuthenticationException("해당 예약 대기에 대한 권한이 없습니다");
        }
    }

    private Schedule findSchedule(Long scheduleId) {
        try {
            return scheduleDao.findById(scheduleId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 스케줄입니다.");
        }
    }

    private Member findMember(Long memberId) {
        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 멤버입니다.");
        }
    }

    private Reservation findReservation(Long reservationId) {
        try {
            return reservationDao.findById(reservationId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 예약입니다.");
        }
    }

    private ReservationWaiting findReservationWaiting(Long reservationWaitingId) {
        try {
            return reservationDao.findWaitingById(reservationWaitingId);
        } catch (EmptyResultDataAccessException e) {
            throw new NullPointerException("존재하지 않는 예약 대기입니다.");
        }
    }
}
