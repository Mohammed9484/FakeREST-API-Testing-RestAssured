package Tests;

import Base.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class UserTests extends Variables {


    public UserTests() throws IOException {
    }

    @Test
    public void getAllUsers() {
        infoHeader.put("Accept", "application/json");
        Response response = (Response) given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .when()
                .get("/Users")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .time(lessThan(6000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("userName", not(isEmptyOrNullString()))
                .body("password", not(isEmptyOrNullString()))
                .extract().response();

    }

    @Test
    public void postNewUser() throws IOException {
        JsonNode jsonNode = mapper.readTree(newUserBody);
        infoHeader.put("Accept", "application/json");
        int userId = jsonNode.get("id").asInt();
        String userName = jsonNode.get("userName").asText();
        String userPassword = jsonNode.get("password").asText();

        Response response = given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .body(newUserBody)
                .when()
                .post("/Users")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(userId))
                .body("userName", equalTo(userName))
                .body("password", equalTo(userPassword))
                .extract().response();

    }

    @Test
    public void getUserById() throws IOException {
        infoHeader.put("Accept", "application/json");
        int targetUserId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", targetUserId)
                .baseUri(baseUrl)
                .when()
                .get("/Users/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(targetUserId))
                .extract().response();
    }

    @Test
    public void editUser() throws IOException {
        JsonNode jsonNode = mapper.readTree(editUserBody);
        int userId = jsonNode.get("id").asInt();
        String userName = jsonNode.get("userName").asText();
        String userPassword = jsonNode.get("password").asText();

        int targetBookId = 3;
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", targetBookId)
                .baseUri(baseUrl)
                .body(editUserBody)
                .when()
                .put("/Users/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(userId))
                .body("userName", equalTo(userName))
                .body("password", equalTo(userPassword))
                .extract().response();
    }

    @Test(dependsOnMethods = "postNewUser")
    public void deleteActivity() throws IOException {
        JsonNode jsonNode = mapper.readTree(newUserBody);
        infoHeader.put("Accept", "application/json");
        int userId = jsonNode.get("id").asInt();
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .baseUri(baseUrl)
                .when()
                .delete("/Users/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .extract().response();
        given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", userId)
                .baseUri(baseUrl)
                .when()
                .get("/Users/{id}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}