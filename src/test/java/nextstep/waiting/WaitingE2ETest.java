package nextstep.waiting;

import static org.assertj.core.api.Assertions.assertThat;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.member.MemberRequest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class WaitingE2ETest extends AbstractE2ETest {
    private TokenResponse userToken;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MemberRequest userMember = new MemberRequest("user", "password", "name", "phone", "USER");
        createMemberRequest(userMember);
        userToken = createTokenRequest(new TokenRequest(userMember.getUsername(), userMember.getPassword()));

        themeId = createThemeRequest(new ThemeRequest("테마이름", "테마설명", 22000));
        scheduleId = createScheduleRequest(new ScheduleRequest(themeId, "2022-08-11", "13:00"));
        createReservationRequest(new ReservationRequest(scheduleId), adminToken.getAccessToken());
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void create() {
        var request = new WaitingRequest(scheduleId);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        String[] reservationLocation = response.header("Location").split("/");
        long resultId = Long.parseLong(reservationLocation[reservationLocation.length - 1]);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/reservation-waitings/" + resultId);
    }

    @DisplayName("이미 해당 유저가 해당 스케줄에 예약이 있는 경우 예약 대기를 할 수 없다.")
    @Test
    void createWithReservedAlready() {
        var reservedScheduleId = createScheduleRequest(new ScheduleRequest(themeId, "2022-08-12", "13:00"));
        createReservationRequest(new ReservationRequest(reservedScheduleId), userToken.getAccessToken());

        var request = new WaitingRequest(reservedScheduleId);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 해당 유저가 해당 스케줄에 예약 대기가 있는 경우 예약 대기를 할 수 없다.")
    @Test
    void createWithWaitingAlready() {
        var waitingScheduleId = createScheduleRequest(new ScheduleRequest(themeId, "2022-08-12", "13:00"));
        createReservationRequest(new ReservationRequest(waitingScheduleId), adminToken.getAccessToken());
        createWaiting(new WaitingRequest(waitingScheduleId), userToken.getAccessToken());

        var request = new WaitingRequest(waitingScheduleId);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비로그인 사용자는 예약 대기를 생성할 수 없다")
    @Test
    void createWithoutLogin() {
        var request = new WaitingRequest(scheduleId);
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약이 존재하지 않는 스케줄에 대기 신청 시 예약으로 전환된다.")
    @Test
    void createEmptySchedule() {
        var emptyScheduleId = createScheduleRequest(new ScheduleRequest(themeId, "2022-08-12", "13:00"));
        var request = new WaitingRequest(emptyScheduleId);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        String[] reservationLocation = response.header("Location").split("/");
        long resultId = Long.parseLong(reservationLocation[reservationLocation.length - 1]);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/reservations/" + resultId);
    }

    @DisplayName("예약 대기 목록을 조회한다")
    @Test
    void showMine() {
        createWaiting(new WaitingRequest(scheduleId), userToken.getAccessToken());

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<WaitingResponse> waitingResponses = response.jsonPath().getList(".", WaitingResponse.class);
        assertThat(waitingResponses.size()).isEqualTo(1);
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void delete() {
        var waitingId = createWaiting(new WaitingRequest(scheduleId), userToken.getAccessToken());
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("타인의 예약 대기를 삭제할 수 없다")
    @Test
    void deleteOthers() {
        MemberRequest other = new MemberRequest("other", "password", "name", "phone", "USER");
        createMemberRequest(other);
        TokenResponse otherToken = createTokenRequest(new TokenRequest(other.getUsername(), other.getPassword()));

        var waitingId = createWaiting(new WaitingRequest(scheduleId), userToken.getAccessToken());
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(otherToken.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("없는 예약 대기를 삭제한다")
    @Test
    void deleteNoExistentWaiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .when().delete("/reservation-waitings/" + 1)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
