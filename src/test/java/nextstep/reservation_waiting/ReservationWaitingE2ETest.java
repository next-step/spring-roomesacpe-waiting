package nextstep.reservation_waiting;

import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationE2ETest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.reservation.ReservationE2ETest.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationWaitingE2ETest extends AbstractE2ETest {

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

    @DisplayName("이미 예약이 존재하는 스케줄에 예약대기를 생성한다.")
    @Test
    void createReservationWaiting() {
        // given
        예약을_생성한다(scheduleId, token);

        var request = new ReservationWaitingRequest(scheduleId);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 없는 스케줄에 예약대기를 생성하면, 예약을 생성한다.")
    @Test
    void createReservationWaitingWithNotExistsReservationSchedule() {
        // given
        var request = new ReservationWaitingRequest(scheduleId);
        var getReservationsResponse1 = 예약을_조회한다(themeId);
        assertThat(getReservationsResponse1.jsonPath().getList("id")).isEmpty();

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var getReservationsResponse = 예약을_조회한다(themeId);
        assertThat(getReservationsResponse.jsonPath().getList("id")).hasSize(1);
    }

    @DisplayName("예약 대기를 삭제한다.")
    @Test
    void deleteReservationWaiting() {
        // given
        예약을_생성한다(scheduleId, token);
        ExtractableResponse<Response> createResponse = 예약대기를_생성한다(new ReservationWaitingRequest(scheduleId), token);
        Long reservationWaitingId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        // when
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .pathParam("reservationWaitingId", reservationWaitingId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/reservation-waitings/{reservationWaitingId}")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 예약대기를_생성한다(ReservationWaitingRequest request, TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }
}
