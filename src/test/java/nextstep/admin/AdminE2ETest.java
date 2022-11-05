package nextstep.admin;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationStatus;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;
    private Long reservationId;

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

        ExtractableResponse<Response> reservationResponse = ReservationE2ETest.예약을_생성한다(scheduleId, token);
        String[] reservationLocation = reservationResponse.header("Location").split("/");
        reservationId = Long.parseLong(reservationLocation[reservationLocation.length - 1]);
    }

    @DisplayName("관리자 권한으로 예약 승인이 가능하다.")
    @Test
    void approveReservation() {
        // when
        var response = RestAssured
                .given().log().all()
                .pathParam("reservationId", reservationId)
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/admin/reservations/{reservationId}/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        var getReservationResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservations/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(getReservationResponse.jsonPath().getList("status"))
                .containsExactly(ReservationStatus.APPROVE.name());
    }
}
