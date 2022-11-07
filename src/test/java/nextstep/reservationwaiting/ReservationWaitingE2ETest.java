package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThat;

import auth.TokenResponse;
import io.restassured.RestAssured;
import java.util.List;
import java.util.regex.Pattern;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ReservationWaitingE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(themeRequest)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        var scheduleResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    @DisplayName("예약이 있는 스케줄에 예약 대기를 생성한다")
    @Test
    void createToBookedSchedule() {
        // given
        createReservation(new ReservationRequest(scheduleId), otherToken);

        // when
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(new ReservationWaitingRequest(scheduleId))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).matches(Pattern.compile("/reservation-waitings/\\d+"));
    }

    @DisplayName("예약이 없는 스케줄에 예약 대기하면 예약이 된다")
    @Test
    void createToEmptySchedule() {
        // when
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(new ReservationWaitingRequest(scheduleId))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).matches(Pattern.compile("/reservations/\\d+"));
    }

    @DisplayName("자신의 예약 대기를 취소할 수 있다")
    @Test
    void cancelMine() {
        // given
        createReservation(new ReservationRequest(scheduleId), otherToken);
        Long createdId = createReservationWaiting(new ReservationWaitingRequest(scheduleId), token);

        // when
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete("/reservation-waitings/{id}", createdId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약을 삭제한다")
    @Test
    void createNotExistReservation() {
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete("/reservation-waitings/999")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("자신의 예약 대기가 아니면 취소할 수 없다")
    @Test
    void cancelNotMine() {
        // given
        createReservation(new ReservationRequest(scheduleId), token);
        Long createdId = createReservationWaiting(new ReservationWaitingRequest(scheduleId), otherToken);

        // when
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete("/reservation-waitings/{id}", createdId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("자신의 취소되지 않은 예약 대기 목록을 조회한다")
    @Test
    void readMine() {
        // given
        createReservation(new ReservationRequest(scheduleId), otherToken);
        createReservationWaiting(new ReservationWaitingRequest(scheduleId), otherToken);
        Long createdId = createReservationWaiting(new ReservationWaitingRequest(scheduleId), token);
        cancelReservationWaiting(createdId);
        createReservationWaiting(new ReservationWaitingRequest(scheduleId), token);

        // when
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get("/reservation-waitings/mine")
            .then().log().all()
            .extract();

        // then
        List<ReservationWaitingResponse> reservationWaitingResponses =
            response.jsonPath().getList(".", ReservationWaitingResponse.class);

        assertThat(reservationWaitingResponses).hasSize(1);
        assertThat(reservationWaitingResponses.get(0))
            .extracting(ReservationWaitingResponse::getWaitNum)
            .isEqualTo(2L);
    }

    private void createReservation(ReservationRequest reservationRequest, TokenResponse tokenResponse) {
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(reservationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long createReservationWaiting(
        ReservationWaitingRequest reservationWaitingRequest, TokenResponse tokenResponse
    ) {
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .body(reservationWaitingRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String[] location = response.header("Location").split("/");
        return Long.parseLong(location[location.length - 1]);
    }

    private void cancelReservationWaiting(Long id) {
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete("/reservation-waitings/{id}", id)
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
