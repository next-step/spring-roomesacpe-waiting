package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThat;

import auth.TokenResponse;
import io.restassured.RestAssured;
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

    }

    @DisplayName("자신의 예약 대기가 아니면 취소할 수 없다")
    @Test
    void cancelNotMine() {

    }

    // TODO ggyool 순번도 비교해야함
    @DisplayName("자신의 예약 목록을 조회할 수 있다")
    @Test
    void readMine() {

    }

    @DisplayName("취소된 예약 대기 목록을 조회할 수 있다")
    @Test
    void readCanceledWaitings() {

    }

    @DisplayName("자신의 예약 대기가 아니면 목록을 조회할 수 없다")
    @Test
    void readNotMine() {

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
}
