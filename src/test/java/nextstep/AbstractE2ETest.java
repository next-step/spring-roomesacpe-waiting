package nextstep;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import nextstep.member.MemberRequest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import nextstep.waiting.WaitingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    protected TokenResponse adminToken;

    @BeforeEach
    protected void setUp() {
        createMemberRequest(new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN"));
        adminToken = createTokenRequest(new TokenRequest(USERNAME, PASSWORD));
    }

    protected void createMemberRequest(MemberRequest memberRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    protected TokenResponse createTokenRequest(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);
    }

    protected Long createWaiting(WaitingRequest waitingRequest, String accessToken) {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        String[] waitingLocation = response.header("Location").split("/");
        return Long.parseLong(waitingLocation[waitingLocation.length - 1]);
    }

    protected Long createThemeRequest(ThemeRequest themeRequest) {
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
        return Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    protected Long createScheduleRequest(ScheduleRequest scheduleRequest) {
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
        return Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    protected Long createReservationRequest(ReservationRequest reservationRequest, String accessToken) {
        var reservationResponse = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] reservationLocation = reservationResponse.header("Location").split("/");
        return Long.parseLong(reservationLocation[reservationLocation.length - 1]);
    }
}
