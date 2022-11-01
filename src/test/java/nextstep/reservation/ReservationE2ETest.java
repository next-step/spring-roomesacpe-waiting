package nextstep.reservation;

import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.dto.MyReservationResponse;
import nextstep.reservation.dto.MyWaitingResponse;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationWaitingRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationE2ETest extends AbstractE2ETest {
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

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("비로그인 사용자가 예약을 생성한다")
    @Test
    void createWithoutLogin() {
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void createWaitingReservation() {
        createReservation(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(new ReservationWaitingRequest(scheduleId))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/reservation-waitings");
    }

    @DisplayName("예약 대기를 생성하는데 예약이 된다.")
    @Test
    void tryCreateWaiting_reserve() {
        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(new ReservationWaitingRequest(scheduleId))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/reservations");
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {
        createReservation(token);

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("내 예약을 조회한다")
    @Test
    void showMine() {
        createReservation(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get("/reservations/mine")
            .then().log().all()
            .extract();

        List<MyReservationResponse> reservations = response.jsonPath().getList(".", MyReservationResponse.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("내 웨이팅을 조회한다")
    @Test
    void showWaitingMine() {
        createReservation(token);
        createWaiting();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get("/reservation-waitings/mine")
            .then().log().all()
            .extract();

        List<MyWaitingResponse> reservations = response.jsonPath().getList(".", MyWaitingResponse.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        var reservation = createReservation(token);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void deleteWaiting() {
        createReservation(token);
        var waiting = createWaiting();

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete(waiting.header("Location"))
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createReservation(token);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약이 없을 때 예약 목록을 조회한다")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("없는 예약을 삭제한다")
    @Test
    void createNotExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람이 예약을삭제한다")
    @Test
    void deleteReservationOfOthers() {
        createReservation(token);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 승인한다.")
    @Test
    void approveReservation() {
        var reservation = createReservation(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().patch(reservation.header("Location")+ "/approve")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.CONFIRMED);
    }

    @DisplayName("예약을 취소한다. (admin : WAIT -> CANCEL)")
    @Test
    void cancelWaitReservation_admin() {
        var reservation = createReservation(token);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().patch(reservation.header("Location")+ "/cancel")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.CANCEL);
    }

    @DisplayName("예약을 취소한다. (admin : CONFIRMED -> CANCEL)")
    @Test
    void cancelConfirmedReservation_admin() {
        var reservation = createReservation(token);
        approve(reservation.header("Location") + "/approve");

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().patch(reservation.header("Location")+ "/cancel")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.CANCEL);
    }

    @DisplayName("예약을 취소한다. (member : WAIT -> CANCEL)")
    @Test
    void cancelWaitReservation_member() {
        var reservation = createReservation(memberToken);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(memberToken.getAccessToken())
            .when().patch(reservation.header("Location")+ "/cancel")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.CANCEL);
    }

    @DisplayName("예약을 취소한다. (member : CONFIRMED -> WAIT_CANCEL)")
    @Test
    void cancelConfirmedReservation_member() {
        var reservation = createReservation(memberToken);
        approve(reservation.header("Location") + "/approve");

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(memberToken.getAccessToken())
            .when().patch(reservation.header("Location")+ "/cancel")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.WAIT_CANCEL);
    }

    @DisplayName("예약취소를 허가한다.")
    @Test
    void approveCancel() {
        var reservation = createReservation(memberToken);
        approve(reservation.header("Location") + "/approve");
        memberCancel(reservation);

        var response = RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get(reservation.header("Location")+ "/cancel-approve")
            .then().log().all()
            .extract();

        Reservation findReservation = findReservation(reservation.header("Location"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findReservation.getStatus()).isEqualTo(Reservation.Status.CANCEL);
    }

    private ExtractableResponse<Response> memberCancel(ExtractableResponse<Response> reservation) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(memberToken.getAccessToken())
            .when().patch(reservation.header("Location") + "/cancel")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> createReservation(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createWaiting() {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservation-waitings")
            .then().log().all()
            .extract();
    }

    private Reservation findReservation(String path) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().get(path)
            .then().log().all()
            .extract()
            .as(Reservation.class);
    }

    private ExtractableResponse<Response> approve(String path) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().patch(path)
            .then().log().all()
            .extract();
    }

}
