package roomescape.member;

import io.restassured.RestAssured;
import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.support.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private TokenResponse token;

    @BeforeEach
    void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);
    }

    @DisplayName("멤버를 생성한다")
    @Test
    void create() {
        // given
        MemberRequest body = new MemberRequest("new_username", "password", "name", "010-1234-5678", "ADMIN");

        // when
        ExtractableResponse<Response> response = createMember(body);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("중복이름으로 가입 시, 예외를 반환한다")
    @Test
    void failToCreate() {
        // given
        MemberRequest body = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");

        // when
        ExtractableResponse<Response> response = createMember(body);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("이미 사용중인 username 입니다.");
    }

    @DisplayName("내 정보를 조회한다")
    @Test
    void showThemes() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Member member = response.as(Member.class);
        assertThat(member.getUsername()).isNotNull();
    }

    private ExtractableResponse<Response> createMember(MemberRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();
    }
}
