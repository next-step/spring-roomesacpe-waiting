package roomescape.reservation;

import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.AbstractE2ETest;
import roomescape.schedule.ScheduleRequest;
import roomescape.theme.ThemeRequest;
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
                .auth().oauth2(adminToken.getAccessToken())
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
                .auth().oauth2(adminToken.getAccessToken())
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
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("중복 예약을 생성하면 예외를 반환한다")
    @Test
    void createDuplicateReservation() {
        createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비로그인 사용자가 예약을 생성하면 예외를 반환한다")
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
    void createWaiting() {
        createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 없을 때, 예약대기를 신청할 경우 예약이 생성된다")
    @Test
    void createReservationWhenNoWaiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        var reservations = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract().jsonPath().getList(".", Reservation.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {
        createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservations = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservations).hasSize(1);
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

        List<ReservationResponse> reservations = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("예약을 승인한다")
    @Test
    void approve() {
        var reservation = createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 사람이 예약을 승인하면, 예외를 반환한다")
    @Test
    void failToApprove() {
        var reservation = createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 취소한다 - 관리자")
    @Test
    void cancelReservationByAdmin() {
        var reservation = createReservation(notAdminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/cancel")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("예약을 취소한다 - 사용자")
    @Test
    void cancelReservationByUser() {
        var reservation = createReservation(notAdminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/cancel")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("예약을 취소요청을 승인한다")
    @Test
    void approveCancelReservation() {
        var reservation = createReservation(notAdminToken);
        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/approve")
                .then().log().all()
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/cancel")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().get(reservation.header("Location") + "/cancel-approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 사람이 예약취소를 승인하면, 예외를 반환한다")
    @Test
    void approveCancelReservationByUser() {
        var reservation = createReservation(notAdminToken);
        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/approve")
                .then().log().all()
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken.getAccessToken())
                .when().patch(reservation.header("Location") + "/cancel")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken.getAccessToken())
                .when().get(reservation.header("Location") + "/cancel-approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        var reservation = createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약을 삭제하면 예외를 반환한다")
    @Test
    void deleteNotExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람이 예약을 삭제하면 예외를 반환한다")
    @Test
    void deleteReservationOfOthers() {
        createReservation(adminToken);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약 대기를 취소한다")
    @Test
    void deleteWaiting() {
        createReservation(adminToken);
        var waiting = createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().put(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약대기를 취소하면 예외를 반환한다")
    @Test
    void deleteNotExistWaiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().put("/reservation-waitings/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("취소된 예약대기를 취소하면 예외를 반환한다")
    @Test
    void deleteCanceledWaiting() {
        createReservation(adminToken);
        var waiting = createReservationWaiting();
        cancelWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().put(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람이 예약을 삭제하면 예외를 반환한다")
    @Test
    void deleteWaitingOfOthers() {
        createReservation(adminToken);
        var waiting = createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().put(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> cancelWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().put("/reservation-waitings/1")
                .then().log().all()
                .extract();
    }
}
