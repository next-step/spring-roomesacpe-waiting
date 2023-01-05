package com.nextstep.web.waiting.service;

import com.nextstep.web.member.LoginMember;
import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.waiting.dto.WaitingResponse;
import nextstep.domain.waiting.Waiting;
import nextstep.domain.waiting.WaitingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingQueryService {
    private final WaitingRepository waitingRepository;
    private final MemberDao memberDao;

    public WaitingQueryService(WaitingRepository waitingRepository, MemberDao memberDao) {
        this.waitingRepository = waitingRepository;
        this.memberDao = memberDao;
    }

    public List<WaitingResponse> getWaitings(LoginMember loginMember) {
        MemberEntity memberEntity = memberDao.findById(loginMember.getId())
                .orElseThrow(() -> new RuntimeException("유저가없어요"));
        List<Waiting> waitings = waitingRepository.findByMember(memberEntity.fromThis());
        return WaitingResponse.toList(waitings);
    }
}
