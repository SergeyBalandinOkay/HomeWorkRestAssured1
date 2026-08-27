package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class WdHubStatusTests extends BaseTest {

    @Test
    public void statusTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void valueMessageCheckTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .body("value.message", equalTo("Selenoid v3.0.14 built at 2026-08-25_08:50:12PM"));
    }

    @Test
    public void valueReadyCheckTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .body("value.ready", equalTo(true));
    }

    @Test
    public void statusSchemaTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .body(matchesJsonSchemaInClasspath("schemas/status_response_schema.json"));
    }

    @Test
    public void unauthorizedStatusNegativeTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1235")
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    public void absenceOfALoginAndPasswordNegativeTest() {
        given()
                .log().all()
                .auth().preemptive().basic("", "")
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(401)
                .body(containsString("Authorization Required"));
    }

    @Test
    public void requiredKeysTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .body("value", hasKey("message"))
                .body("value", hasKey("ready"));
    }

    @Test
    public void checkAllTest() {
        given()
                .log().all()
                .auth().preemptive().basic("user1", "1234")
                .when()
                .get("/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("value.message", equalTo("Selenoid v3.0.14 built at 2026-08-25_08:50:12PM"))
                .body("value.ready", equalTo(true))
                .body(matchesJsonSchemaInClasspath("schemas/status_response_schema.json"))
                .body("value", hasKey("message"))
                .body("value", hasKey("ready"));
    }
}