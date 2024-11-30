package tests;

import utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

@Slf4j
@Epic("Pet Store API")
@Feature("Pet Management")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetEndpointNegativeScenarios extends TestBase {

    @Order(1)
    @Test
    @Story("Create Pet")
    @Description("Verify API rejects pets with schema violations")
    @DisplayName("POST - Violate Schema - Negative scenario")

    void testCreatePetInvalidIdSchema() {
        JsonObject invalidIdType = new JsonObject();
        invalidIdType.addProperty("id", "invalid_id");  // Should be integer
        invalidIdType.addProperty("name", "doggie");
        invalidIdType.addProperty("status", "available");

        Response response = given()
                .body(invalidIdType.toString())
                .when()
                .post("/pet")
                .then()
                .statusCode(500)
                .extract().response();

        // Add to Allure report
        Allure.addAttachment("Response", response.getBody().asString());
    }

    @Test
    @Order(2)
    @Story("Retrieve Pet")
    @Description("Non-existent pet cannot be found")
    @DisplayName("GET - Fail to find pet by ID - Negative scenario")
    void testGetNonExistentPetById() {
        Response response = given()
                .pathParam("petId", testPetInvalid.getId())
                .when()
                .get("/pet/{petId}")
                .then()
                .assertThat()
                .statusCode(404)
                .extract().response();

        Allure.addAttachment("Response", response.getBody().asString());
    }

    @Test
    @Order(3)
    @Story("Update Pet")
    @Description("Verify API rejects invalid structure")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PUT - Violate schema - Negative scenario")
    void testUpdatePetWithInvalidTagStructure() {
        JsonObject invalidTags = new JsonObject();
        invalidTags.addProperty("id", 1);
        invalidTags.addProperty("name", "doggie");
        invalidTags.addProperty("status", "available");

        JsonArray tags = new JsonArray();
        JsonObject tag = new JsonObject();
        tag.addProperty("id", "invalid_id");
        tag.addProperty("name", 123);
        tags.add(tag);
        invalidTags.add("tags", tags);

        Response response = given()
                .body(invalidTags.toString())
                .when()
                .put("/pet")
                .then()
                .statusCode(500)
                .extract().response();

        Allure.addAttachment("Invalid Tags Response", response.getBody().asString());
    }

    @Test
    @Order(4)
    @Story("Delete Pet")
    @Description("Verify that an nonexistent pet can't be deleted")
    @DisplayName("DELETE - Can't delete not existing pet - Positive scenario")
    void testDeletePet() {

        Response response = given()
                .pathParam("petId", testPetInvalid.getId())
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(404)
                .extract().response();

        Allure.addAttachment("Delete Response", response.getBody().asString());
    }
}