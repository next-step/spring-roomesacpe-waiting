package com.nextstep.web.waiting.repository;

import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.waiting.repository.entitiy.WaitingEntity;
import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.waiting.Waiting;
import nextstep.domain.waiting.WaitingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcWaitingRepository implements WaitingRepository {
    private final WaitingDao waitingDao;

    public JdbcWaitingRepository(WaitingDao waitingDao) {
        this.waitingDao = waitingDao;
    }


    @Override
    public Waiting save(Waiting waiting) {
         WaitingEntity waitingEntity = new WaitingEntity(waiting.getId().getNumber(), ScheduleEntity.of(waiting.getSchedule()),
                MemberEntity.of(waiting.getMember()), waiting.getWaitingNumber(), waiting.getWaitingStatus().name(),
                waiting.getReservationWaitingTime());
        Long id = waitingDao.save(waitingEntity);
        waitingEntity.setId(id);
        return waitingEntity.fromThis();
    }

    @Override
    public List<Waiting> findByMember(Member member) {
        return waitingDao.findByMember(MemberEntity.of(member)).stream()
                .map(WaitingEntity::fromThis)
                .collect(Collectors.toList());

    }

    @Override
    public List<Waiting> findBySchedule(Schedule schedule) {
        return waitingDao.findBySchedule(ScheduleEntity.of(schedule)).stream()
                .map(WaitingEntity::fromThis)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Waiting waiting) {
        waitingDao.delete(WaitingEntity.of(waiting));
        return;
    }
}
