package com.nextstep.web.waiting.controller;

import com.nextstep.web.auth.LoginMemberPrincipal;
import com.nextstep.web.auth.UserDetail;
import com.nextstep.web.member.LoginMember;
import com.nextstep.web.waiting.dto.WaitingRequest;
import com.nextstep.web.waiting.dto.WaitingResponse;
import com.nextstep.web.waiting.service.WaitingCommandService;
import com.nextstep.web.waiting.service.WaitingQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("reservation-waiting")
public class WaitingController {
    private final WaitingCommandService waitingCommandService;
    private final WaitingQueryService waitingQueryService;

    public WaitingController(WaitingCommandService waitingCommandService, WaitingQueryService waitingQueryService) {
        this.waitingCommandService = waitingCommandService;
        this.waitingQueryService = waitingQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody WaitingRequest waitingRequest, @LoginMemberPrincipal UserDetail userDetail) {
         String uri = waitingCommandService.create(waitingRequest, LoginMember.from(userDetail));
         return ResponseEntity.created(URI.create(uri))
                 .build();
    }

    @GetMapping
    public ResponseEntity<List<WaitingResponse>> read(@LoginMemberPrincipal UserDetail userDetail) {
        return ResponseEntity.ok(waitingQueryService.getWaitings(LoginMember.from(userDetail)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @LoginMemberPrincipal UserDetail userDetail) {
        waitingCommandService.delete(id, LoginMember.from(userDetail));
        return ResponseEntity.noContent().build();
    }
}
