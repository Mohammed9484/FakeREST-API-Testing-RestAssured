package Tests;

import Base.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;


public class BooksTests extends Variables {


    public BooksTests() throws IOException {
    }

    @Test
    public void getAllBooks() {
        infoHeader.put("Accept", "application/json");
        Response response = (Response) given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .when()
                .get("/Books")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .time(lessThan(6000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("title", not(isEmptyOrNullString()))
                .body("description", not(isEmptyOrNullString()))
                .body("pageCount", notNullValue())
                .body("excerpt", not(isEmptyOrNullString()))
                .body("publishDate", not(isEmptyOrNullString()))

                .extract().response();

    }

    @Test
    public void postNewBook() throws IOException {
        JsonNode jsonNode = mapper.readTree(newBookBody);
        infoHeader.put("Accept", "application/json");
        int bookId = jsonNode.get("id").asInt();
        String bookTilte = jsonNode.get("title").asText();
        String bookDescription = jsonNode.get("description").asText();
        int bookPageCount = jsonNode.get("pageCount").asInt();
        String bookExcerpt = jsonNode.get("excerpt").asText();
        String bookPublishdate = jsonNode.get("publishDate").asText();

        Response response = given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .body(newBookBody)
                .when()
                .post("/Books")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(bookId))
                .body("title", equalTo(bookTilte))
                .body("description", equalTo(bookDescription))
                .body("pageCount", equalTo(bookPageCount))
                .body("excerpt", equalTo(bookExcerpt))
                .body("publishDate", equalTo(bookPublishdate))
                .extract().response();

    }

    @Test
    public void getBookByID() throws IOException {
        infoHeader.put("Accept", "application/json");
        int targetBookId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", targetBookId)
                .baseUri(baseUrl)
                .when()
                .get("/Books/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(targetBookId))
                .extract().response();
    }

    @Test
    public void editBook() throws IOException {
        JsonNode jsonNode = mapper.readTree(editBookBody);
        int bookId = jsonNode.get("id").asInt();
        String bookTilte = jsonNode.get("title").asText();
        String bookDescription = jsonNode.get("description").asText();
        int bookPageCount = jsonNode.get("pageCount").asInt();
        String bookExcerpt = jsonNode.get("excerpt").asText();
        String bookPublishdate = jsonNode.get("publishDate").asText();

        int targetBookId = 3;
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", targetBookId)
                .baseUri(baseUrl)
                .body(editBookBody)
                .when()
                .put("/Books/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(bookId))
                .body("title", equalTo(bookTilte))
                .body("description", equalTo(bookDescription))
                .body("pageCount", equalTo(bookPageCount))
                .body("excerpt", equalTo(bookExcerpt))
                .body("publishDate", equalTo(bookPublishdate))
                .extract().response();
    }

    @Test(dependsOnMethods = "postNewBook")
    public void deleteActivity() throws IOException {
        JsonNode jsonNode = mapper.readTree(newBookBody);
        infoHeader.put("Accept", "application/json");
        int bookId = jsonNode.get("id").asInt();
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", bookId)
                .baseUri(baseUrl)
                .when()
                .delete("/Books/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .extract().response();
        given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", bookId)
                .baseUri(baseUrl)
                .when()
                .get("/Books/{id}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}