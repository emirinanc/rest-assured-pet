package tests;

import configuration.ConfigurationManager;
import io.restassured.RestAssured;
import models.User;
import models.Pet;
import utils.RestAssuredRequestFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import lombok.extern.slf4j.Slf4j;
import utils.TestDataGenerator;

import static io.restassured.RestAssured.given;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {
    protected RequestSpecification requestSpec;
    protected ConfigurationManager config;
    protected User testUser;
    protected Pet testPet;
    protected Pet testPetInvalid;

    @BeforeAll
    public void setup() {
        config = ConfigurationManager.getInstance();

        testUser = createTestUser();

        testPet = TestDataGenerator.generatePet();

        testPetInvalid = TestDataGenerator.generatePet("personaNonGrata","invalidStatus");

        // Setup base request specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(config.getProperty("base.url"))
                .setContentType(ContentType.JSON)
                .addHeader("api_key",config.getProperty("api.key"))
                .addFilter(new AllureRestAssured())
                .addFilter(new RestAssuredRequestFilter())
                .build();

        RestAssured.requestSpecification = requestSpec;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Create and login test user
        createAndLoginUser(testUser);
    }

    private User createTestUser() {
        return User.builder()
                .username(config.getProperty("test.user.username", "testuser"))
                .password(config.getProperty("test.user.password", "Test123!"))
                .firstName("Test")
                .lastName("User")
                .email("testuser@example.com")
                .phone("1234567890")
                .userStatus(1)
                .build();
    }

    protected void createAndLoginUser(User user) {

        Response createResponse = given(requestSpec)
                .body(user)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .extract().response();

        log.info("User created: {}", createResponse.body().asString());


        Response loginResponse = given(requestSpec)
                .queryParam("username", user.getUsername())
                .queryParam("password", user.getPassword())
                .when()
                .get("/user/login")
                .then()
                .statusCode(200)
                .extract().response();

        log.info("User logged in: {}", loginResponse.body().asString());

    }

}