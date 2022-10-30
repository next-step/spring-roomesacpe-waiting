package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationResponse;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ReservationWaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
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

        request = new ReservationRequest(
                scheduleId
        );
    }

    @DisplayName("예약이 없다면 예약을 생성한다")
    @Test
    void createReservationTest() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).contains("/reservations/");
    }

    @DisplayName("예약이 있다면 예약대기를 생성한다")
    @Test
    void createReservationWaitingTest() {
        createReservation();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).contains("/reservation-waitings/");
    }

    @DisplayName("나의 예약대기를 조회한다")
    @Test
    void showMine() {
        createReservation();
        createReservationWaiting();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get("/reservation-waitings/mine")
            .then().log().all()
            .extract();

        List<ReservationWaitingDetails> results = response.jsonPath().getList(".", ReservationWaitingDetails.class);
        assertThat(results.size()).isEqualTo(1);
    }

    @DisplayName("비로그인 사용자가 예약 대기를 생성한다")
    @Test
    void createWithoutLoginTest() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("나의 예약대기를 취소한다")
    @Test
    void delete() {
        createReservation();
        var reservationWaiting = createReservationWaiting();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().put(reservationWaiting.header("Location"))
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("다른 사람의 예약대기를 취소한다")
    @Test
    void deleteOthers() {
        createReservation();
        var reservationWaiting = createReservationWaiting();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2("other-token")
            .when().put(reservationWaiting.header("Location"))
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> createReservation() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
    }
}
