package nextstep.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.schedule.Schedule;

public class ScheduleResponse {

    private final Long id;
    private final ThemeResponse theme;
    private final LocalDate date;
    private final LocalTime time;

    public ScheduleResponse(Long id, ThemeResponse theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
            schedule.getId(),
            ThemeResponse.from(schedule.getTheme()),
            schedule.getDate(),
            schedule.getTime()
        );
    }

    public Long getId() {
        return id;
    }

    public ThemeResponse getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
