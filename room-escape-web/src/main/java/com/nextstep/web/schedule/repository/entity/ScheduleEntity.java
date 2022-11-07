package com.nextstep.web.schedule.repository.entity;

import com.nextstep.web.theme.repository.entity.ThemeEntity;
import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleEntity {
    private Long id;
    private ThemeEntity themeEntity;
    private LocalDate date;
    private LocalTime time;

    public ScheduleEntity(Long id, ThemeEntity themeEntity, LocalDate date, LocalTime time) {
        this.id = id;
        this.themeEntity = themeEntity;
        this.date = date;
        this.time = time;
    }

    public static ScheduleEntity of(Schedule schedule) {
        return new ScheduleEntity(schedule.getId().getNumber(), ThemeEntity.of(schedule.getTheme()),
                schedule.getDate(), schedule.getTime());
    }

    public Schedule fromThis() {
        return new Schedule(new Identity(id), themeEntity.fromThis(), date, time);
    }
}
