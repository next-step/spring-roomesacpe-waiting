package com.nextstep.web.waiting.dto;

import com.nextstep.web.schedule.dto.ScheduleResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.waiting.Waiting;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class WaitingResponse {
    private Long id;
    private ScheduleResponse schedule;
    private int waitNum;

    public WaitingResponse(Long id, ScheduleResponse schedule, int waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public static List<WaitingResponse> toList(List<Waiting> waitings) {
        return waitings.stream()
                .map(waiting -> new WaitingResponse(waiting.getId().getNumber(), ScheduleResponse.from(waiting.getSchedule()),
                        waiting.getWaitingNumber()))
        .collect(Collectors.toList());
    }
}
