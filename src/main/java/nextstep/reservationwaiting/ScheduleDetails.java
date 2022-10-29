package nextstep.reservationwaiting;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleDetails {

    private final Long id;
    private final ThemeDetails theme;
    private final LocalDate date;
    private final LocalTime time;

    public ScheduleDetails(Long id, ThemeDetails theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public ThemeDetails getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
