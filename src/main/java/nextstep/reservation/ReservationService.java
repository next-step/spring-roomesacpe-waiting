package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationWaitingRequest;
import nextstep.reservation.dto.ReservationWaitingResponse;
import nextstep.revenue.DailyRevenue;
import nextstep.revenue.RevenueDao;
import nextstep.revenue.RevenueHistory;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final RevenueDao revenueDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, RevenueDao revenueDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.revenueDao = revenueDao;
    }

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        if (memberId == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(memberId);
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
            schedule,
            member
        );

        return reservationDao.save(newReservation);
    }

    public ReservationWaitingResponse createWaiting(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        if (memberId == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(memberId);
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        int seq = reservationDao.findLastWaitingSeq(schedule.getId());
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (reservation.isEmpty() && seq == 0) {
            // 예약이 없고 예약 대기도 없을 경우
            return new ReservationWaitingResponse(reservationDao.save(new Reservation(schedule, member)), true);
        } else {
            ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, ++seq);
            return new ReservationWaitingResponse(reservationDao.saveWaiting(reservationWaiting), false);
        }
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(memberId)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public void deleteWaitingById(long memberId, Long id) {
        ReservationWaiting reservationWaiting = reservationDao.findWaitingById(id);
        if (reservationWaiting == null) {
            throw new NullPointerException();
        }

        if (!reservationWaiting.sameMember(memberId)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteWaitingById(id);
    }

    public void approve(Long memberId, Long reservationId, LocalDateTime at) {
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null) {
            throw new NullPointerException();
        }

        reservation.approve();
        reservationDao.updateStatus(reservation);

        long profit = reservation.getSchedule().getTheme().getPrice();
        Optional<DailyRevenue> dailyRevenue = revenueDao.findDailyRevenueAt(at.toLocalDate());
        dailyRevenue.ifPresentOrElse(
            revenue -> {
                Long originalProfit = revenue.getProfit();
                revenue.plusProfit(profit);

                revenueDao.updateProfit(revenue);
                revenueDao.saveHistory(new RevenueHistory(revenue.getId(), originalProfit, revenue.getProfit(), reservation.getStatus().name(), at));
            },
            () -> {
                DailyRevenue newRevenue = new DailyRevenue(memberId, profit, at.toLocalDate());
                Long id = revenueDao.save(newRevenue);
                revenueDao.saveHistory(new RevenueHistory(id, 0L, profit, reservation.getStatus().name(), at));
            }
        );
    }

    public List<Reservation> findMyReservations(Long memberId) {
        return reservationDao.findByMemberId(memberId);
    }

    public List<ReservationWaiting> findMyWaitings(Long memberId) {
        return reservationDao.findWaitingsByMemberId(memberId);
    }
}
