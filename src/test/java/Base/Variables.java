package Base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import java.util.HashMap;

public class Variables  {
    public ObjectMapper mapper = new ObjectMapper();
    public String baseUrl = "https://fakerestapi.azurewebsites.net/api/v1";
    //headers Hashmap
    public HashMap<String, String> infoHeader = new HashMap<>();
    //New Activity Body
    public File newActivityBody = new File("src/test/resources/newActivity.json");
    //Edit Activity Body
    public File editActivityBody = new File("src/test/resources/editActivity.json");
    //New Author Body
    public File newAuthorBody = new File("src/test/resources/newAuthor.json");
    //Edit Author Body
    public File editAuthorBody = new File("src/test/resources/editAuthor.json");
    //New Book Body
    public File newBookBody = new File("src/test/resources/newBook.json");
    //Edit Book Body
    public File editBookBody = new File("src/test/resources/editBook.json");
    //New CoverPhoto Body
    public File newCoverPhotoBody = new File("src/test/resources/newCoverPhoto.json");
    //Edit CoverPhoto Body
    public File editCoverPhotoBody = new File("src/test/resources/editCoverPhoto.json");
    //New User Body
    public File newUserBody = new File("src/test/resources/newUser.json");
    //Edit User Body
    public File editUserBody = new File("src/test/resources/editUser.json");



}
