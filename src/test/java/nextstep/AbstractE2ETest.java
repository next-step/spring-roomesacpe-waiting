package nextstep;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import nextstep.member.MemberRequest;
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

    public static final String OTHER_USERNAME = "otherusername";
    public static final String OTHER_PASSWORD = "otherpassword";

    protected TokenResponse token, otherToken;

    @BeforeEach
    protected void setUp() {
        createMember(
            new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN")
        );
        createMember(
            new MemberRequest(OTHER_USERNAME, OTHER_PASSWORD, "otherName", "010-8765-4321", "ADMIN")
        );

        token = login(new TokenRequest(USERNAME, PASSWORD));
        otherToken = login(new TokenRequest(OTHER_USERNAME, OTHER_PASSWORD));
    }

    private void createMember(MemberRequest memberRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    private TokenResponse login(TokenRequest tokenBody) {
        var response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenBody)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        return response.as(TokenResponse.class);
    }
}
