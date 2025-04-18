package Tests;

import Base.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CoverPhotosTests extends Variables {


    public CoverPhotosTests() throws IOException {
    }

    @Test
    public void getAllPhotos() {
        infoHeader.put("Accept", "application/json");
        Response response = (Response) given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .when()
                .get("/CoverPhotos")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .time(lessThan(6000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("idBook", notNullValue())
                .body("url", not(isEmptyOrNullString()))
                .extract().response();

    }

    @Test
    public void postNewPhoto() throws IOException {
        JsonNode jsonNode = mapper.readTree(newCoverPhotoBody);
        infoHeader.put("Accept", "application/json");
        int photoId = jsonNode.get("id").asInt();
        int bookId = jsonNode.get("idBook").asInt();
        String photoUrl = jsonNode.get("url").asText();


        Response response = given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .body(newCoverPhotoBody)
                .when()
                .post("/CoverPhotos")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(photoId))
                .body("idBook", equalTo(bookId))
                .body("url", equalTo(photoUrl))
                .extract().response();

    }
    @Test
    public void getPhotoByBookId() throws IOException {
        infoHeader.put("Accept", "application/json");
        int targetBookId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", targetBookId)
                .baseUri(baseUrl)
                .when()
                .get("/CoverPhotos/books/covers/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("idBook", everyItem(equalTo(targetBookId)))
                .body("url", not(isEmptyOrNullString()))
                .extract().response();
    }

    @Test
    public void getPhotoById() throws IOException {
        infoHeader.put("Accept", "application/json");
        int targetPhotoId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", targetPhotoId)
                .baseUri(baseUrl)
                .when()
                .get("/CoverPhotos/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(targetPhotoId))
                .extract().response();
    }

    @Test
    public void editAuthor() throws IOException {
        JsonNode jsonNode = mapper.readTree(editCoverPhotoBody);
        infoHeader.put("Accept", "application/json");
        int photoId = jsonNode.get("id").asInt();
        int bookId = jsonNode.get("idBook").asInt();
        String photoUrl = jsonNode.get("url").asText();

        int TargetAuthorId = 3;
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", TargetAuthorId)
                .baseUri(baseUrl)
                .body(editCoverPhotoBody)
                .when()
                .put("/CoverPhotos/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(photoId))
                .body("idBook", equalTo(bookId))
                .body("url", equalTo(photoUrl))
                .extract().response();
    }

    @Test(dependsOnMethods = "postNewPhoto")
    public void deleteAuthor() throws IOException {
        JsonNode jsonNode = mapper.readTree(newCoverPhotoBody);
        infoHeader.put("Accept", "application/json");
        int photoId = jsonNode.get("id").asInt();
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", photoId)
                .baseUri(baseUrl)
                .when()
                .delete("/CoverPhotos/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .extract().response();
        given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", photoId)
                .baseUri(baseUrl)
                .when()
                .get("/CoverPhotos/{id}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}