package nextstep;

import auth.UserDetail;
import nextstep.member.Member;
import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.ScheduleRequest;
import nextstep.schedule.ScheduleService;
import nextstep.theme.ThemeRequest;
import nextstep.theme.ThemeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class StartUpRunner implements ApplicationRunner {

    private final ThemeService themeService;
    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final ReservationService reservationService;

    public StartUpRunner(
            ThemeService themeService,
            MemberService memberService,
            ScheduleService scheduleService,
            ReservationService reservationService
    ) {
        this.themeService = themeService;
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.reservationService = reservationService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Long memberId = memberService.create(
                new MemberRequest(
                        "username",
                        "password",
                        "name",
                        "010-1234-1234",
                        "ADMIN"
                )
        );

        Long memberId2 = memberService.create(
                new MemberRequest(
                        "username2",
                        "password",
                        "name",
                        "010-1234-1234",
                        "ADMIN"
                )
        );

        Long memberId5 = memberService.create(
                new MemberRequest(
                        "username5",
                        "password",
                        "name",
                        "010-1234-1234",
                        "ADMIN"
                )
        );

        Long memberId3 = memberService.create(
                new MemberRequest(
                        "username3",
                        "password",
                        "name",
                        "010-1234-1234",
                        "ADMIN"
                )
        );

        Long memberId4 = memberService.create(
                new MemberRequest(
                        "username4",
                        "password",
                        "name",
                        "010-1234-1234",
                        "ADMIN"
                )
        );

        Long themeId = themeService.create(new ThemeRequest("name", "desc", 1000));
        Long scheduleId = scheduleService.create(new ScheduleRequest(themeId, "2022-08-11", "13:00"));

        Member member = memberService.findById(memberId);
        reservationService.create(
                new UserDetail(member.getUsername(), member.getRole()),
                new ReservationRequest(scheduleId)
        );
    }
}
