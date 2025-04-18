package Tests;

import Base.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ActivitiesTests extends Base.Variables {


    public ActivitiesTests() throws IOException {
    }

    @Test
    public void getAllActivities() {
        infoHeader.put("Accept", "application/json");
        Response response = (Response) given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .when()
                .get("/Activities")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .time(lessThan(6000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", hasSize(30))
                .body("id", notNullValue())
                .body("title", not(isEmptyOrNullString()))
                .body("completed", everyItem(is(oneOf(true, false))))
                .extract().response();

    }

    @Test
    public void postNewActivity() throws IOException {
        JsonNode jsonNode = mapper.readTree(newActivityBody);
        infoHeader.put("Accept", "application/json");
        int activityId = jsonNode.get("id").asInt();
        String activityTitle = jsonNode.get("title").asText();
        String activityDueDate = jsonNode.get("dueDate").asText();
        boolean activityCompleted = jsonNode.get("completed").asBoolean();


        Response response = given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .body(newActivityBody)
                .when()
                .post("/Activities")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(activityId ))
                .body("title", equalToIgnoringCase(activityTitle))
                .body("dueDate", equalToIgnoringCase(activityDueDate))
                .body("completed", equalTo(activityCompleted))
                .extract().response();

    }

    @Test
    public void getActivityById() throws IOException {
        infoHeader.put("Accept", "application/json");
        int targetActivityId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", targetActivityId)
                .baseUri(baseUrl)
                .when()
                .get("/Activities/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(targetActivityId))
                .body("title", not(isEmptyOrNullString()))
                .body("dueDate", notNullValue())
                .body("completed", anyOf(equalTo(true), equalTo(false)))

                .extract().response();
    }

    @Test
    public void editActivity() throws IOException {
        JsonNode jsonNode = mapper.readTree(editActivityBody);
        int activityId = jsonNode.get("id").asInt();
        String activityTitle = jsonNode.get("title").asText();
        String activityDueDate = jsonNode.get("dueDate").asText();
        boolean activityCompleted = jsonNode.get("completed").asBoolean();
        infoHeader.put("Accept", "application/json");
        int targetActivityId = 3;
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", targetActivityId)
                .baseUri(baseUrl)
                .body(editActivityBody)
                .when()
                .put("/Activities/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(activityId))
                .body("title", equalTo(activityTitle))
                .body("dueDate", equalTo(activityDueDate))
                .body("completed", equalTo(activityCompleted))
                .extract().response();
    }

    @Test(dependsOnMethods = "postNewActivity")
    public void deleteActivity() throws IOException {
        JsonNode jsonNode = mapper.readTree(newActivityBody);
        infoHeader.put("Accept", "application/json");
        int activityId = jsonNode.get("id").asInt();
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", activityId)
                .baseUri(baseUrl)
                .when()
                .delete("/Activities/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .extract().response();
        given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", activityId)
                .baseUri(baseUrl)
                .when()
                .get("/Activities/{id}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}