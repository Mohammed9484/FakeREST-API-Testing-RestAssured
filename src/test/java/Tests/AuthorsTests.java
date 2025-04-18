package Tests;

import Base.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class AuthorsTests extends Variables {


    public AuthorsTests() throws IOException {
    }

    @Test
    public void getAllAuthors() {
        infoHeader.put("Accept", "application/json");
        Response response = (Response) given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .when()
                .get("/Authors")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .time(lessThan(6000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("idBook", notNullValue())
                .body("firstName", not(isEmptyOrNullString()))
                .body("lastName", not(isEmptyOrNullString()))
                .extract().response();

    }

    @Test
    public void postNewAuthor() throws IOException {
        JsonNode jsonNode = mapper.readTree(newAuthorBody);
        infoHeader.put("Accept", "application/json");
        int authorId = jsonNode.get("id").asInt();
        int authorIdBook = jsonNode.get("idBook").asInt();
        String authorFirstName = jsonNode.get("firstName").asText();
        String authorLastName = jsonNode.get("lastName").asText();


        Response response = given()
                .baseUri(baseUrl)
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .body(newAuthorBody)
                .when()
                .post("/Authors")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(authorId))
                .body("idBook", equalTo(authorIdBook))
                .body("firstName", equalTo(authorFirstName))
                .body("lastName", equalTo(authorLastName))
                .extract().response();

    }
    @Test
    public void getAuthorByBookId() throws IOException {
        infoHeader.put("Accept", "application/json");
        int authorIdBook = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", authorIdBook)
                .baseUri(baseUrl)
                .when()
                .get("/Authors/authors/books/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", notNullValue())
                .body("idBook", everyItem(equalTo(authorIdBook)))
                .body("firstName", not(isEmptyOrNullString()))
                .body("lastName", not(isEmptyOrNullString()))
                .extract().response();
    }

    @Test
    public void getAuthorById() throws IOException {
        infoHeader.put("Accept", "application/json");
        int authorId = 5;
        Response response = given()
                .headers(infoHeader)
                .pathParam("id", authorId)
                .baseUri(baseUrl)
                .when()
                .get("/Authors/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(3000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(authorId))
                .extract().response();
    }

    @Test
    public void editAuthor() throws IOException {
        JsonNode jsonNode = mapper.readTree(editAuthorBody);
        infoHeader.put("Accept", "application/json");
        int authorId = jsonNode.get("id").asInt();
        int authorIdBook = jsonNode.get("idBook").asInt();
        String authorFirstName = jsonNode.get("firstName").asText();
        String authorLastName = jsonNode.get("lastName").asText();

        int TargetAuthorId = 3;
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", TargetAuthorId)
                .baseUri(baseUrl)
                .body(editAuthorBody)
                .when()
                .put("/Authors/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .header("Content-Type","application/json; charset=utf-8; v=1.0")
                .body("$", not(empty()))
                .body("id", equalTo(authorId))
                .body("idBook", equalTo(authorIdBook))
                .body("firstName", equalTo(authorFirstName))
                .body("lastName", equalTo(authorLastName))
                .extract().response();
    }

    @Test(dependsOnMethods = "postNewAuthor")
    public void deleteAuthor() throws IOException {
        JsonNode jsonNode = mapper.readTree(newAuthorBody);
        infoHeader.put("Accept", "application/json");
        int authorId = jsonNode.get("id").asInt();
        Response response = given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", authorId)
                .baseUri(baseUrl)
                .when()
                .delete("/Authors/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .time(lessThan(2000L))
                .extract().response();
        given()
                .headers(infoHeader)
                .contentType(ContentType.JSON)
                .pathParam("id", authorId)
                .baseUri(baseUrl)
                .when()
                .get("/Authors/{id}")
                .then().log().all()
                .assertThat()
                .statusCode(404);
    }
}