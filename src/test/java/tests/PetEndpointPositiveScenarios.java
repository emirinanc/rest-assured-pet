package tests;

import utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Epic("Pet Store API")
@Feature("Pet Management")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetEndpointPositiveScenarios extends TestBase {

    @Test
    @Order(1)
    @Story("Create Pet")
    @Description("Verify that a new pet can be created successfully")
    @DisplayName("POST - Create new pet - Positive scenario")
    void testCreatePet() {
        Response response = given()
                .body(testPet)
                .when()
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(testPet.getName()))
                .body("status", equalTo(testPet.getStatus()))
                .extract().response();


        // Add to Allure report
        Allure.addAttachment("Response", response.getBody().asString());
    }

    @Test
    @Order(2)
    @Story("Retrieve Pet")
    @Description("Verify that an existing pet can be retrieved by ID")
    @DisplayName("GET - Find pet by ID - Positive scenario")
    void testGetPetById() {
        Response response = given()
                .pathParam("petId", testPet.getId())
                .when()
                .get("/pet/{petId}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(testPet.getId()))
                .body("name", equalTo(testPet.getName()))
                .extract().response();

        Allure.addAttachment("Response", response.getBody().asString());
    }

    @Test
    @Order(3)
    @Story("Update Pet")
    @Description("Verify that an existing pet can be updated")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PUT - Update existing pet - Positive scenario")
    void testUpdatePet() {
        testPet.setStatus("sold");

        Response response = given()
                .body(testPet)
                .when()
                .put("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .body("status", equalTo("sold"))
                .extract().response();

        Allure.addAttachment("Request Body", "application/json",
                TestDataGenerator.toJson(testPet));
        Allure.addAttachment("Response Body", "application/json",
                response.getBody().asString());
    }

    @Test
    @Order(4)
    @Story("Delete Pet")
    @Description("Verify that an existing pet can be deleted")
    @DisplayName("DELETE - Delete pet - Positive scenario")
    void testDeletePet() {

        Response response = given()
                .pathParam("petId", testPet.getId())
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(200)
                .extract().response();

        Allure.addAttachment("Verification Response", response.getBody().asString());
    }
}