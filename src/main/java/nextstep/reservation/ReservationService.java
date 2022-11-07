package nextstep.reservation;

import auth.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationCancellationDao reservationCancellationDao;


    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao,
        ReservationCancellationDao reservationCancellationDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.reservationCancellationDao = reservationCancellationDao;
    }

    @Transactional
    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
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

    public boolean existsReservation(Schedule schedule) {
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        return !reservation.isEmpty();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<ReservationResponse> findAllByMember(Member member) {
        return Stream.concat(
            reservationDao.findByMemberId(member.getId())
                .stream()
                .map(it -> new ReservationResponse(it.getId(), it.getSchedule(), false)),
            reservationCancellationDao.findByMemberId(member.getId())
                .stream()
                .map(it -> new ReservationResponse(it.getId(), it.getSchedule(), true))
        ).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
        reservationCancellationDao.save(reservation);
    }
}
