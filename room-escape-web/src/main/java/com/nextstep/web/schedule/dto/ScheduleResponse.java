package com.nextstep.web.schedule.dto;

import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.theme.dto.ThemeResponse;
import com.nextstep.web.theme.repository.entity.ThemeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.domain.schedule.Schedule;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleResponse {
    private Long id;
    private ThemeResponse theme;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    private ScheduleResponse(Long id, ThemeResponse theme, LocalDate date, LocalTime time) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(schedule.getId().getNumber(), ThemeResponse.of(schedule.getTheme()), schedule.getDate(), schedule.getTime());
    }


    public static List<ScheduleResponse> toListFromEntity(List<ScheduleEntity> scheduleEntities, ThemeEntity themeEntity) {
        return scheduleEntities.stream()
                .map(scheduleEntity -> new ScheduleResponse(scheduleEntity.getId(), ThemeResponse.of(themeEntity),
                        scheduleEntity.getDate(), scheduleEntity.getTime()))
                .collect(Collectors.toList());
    }
}
