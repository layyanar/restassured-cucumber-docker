package cucumbertest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;

public class TestngPostTest {

    private static final String ENDPOINT_USER = "http://reqres.in/api/users";

    @BeforeClass
    public void beforeClass() throws FileNotFoundException {
        //Printing log file using Filter, requestSpecification & responseSpecification
        PrintStream fileOutputStream = new PrintStream(new File("restAssured.log"));

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                addFilter(new RequestLoggingFilter(fileOutputStream)).
                addFilter(new ResponseLoggingFilter(fileOutputStream)).
                setBaseUri(ENDPOINT_USER).addHeader("Content-Type", "application/json").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void postTest() {
        HashMap<String, String> data1 = new HashMap<String, String>();
        data1.put("avatar", "https://reqres.in/img/faces/lin1-image.jpg");
        data1.put("email", "lin1.test1@reqres.in");
        data1.put("first_name", "lin1");
        data1.put("id", "1");
        data1.put("last_name", "test1");

        HashMap<String, String> data2 = new HashMap<String, String>();
        data2.put("avatar", "https://reqres.in/img/faces/lin2-image.jpg");
        data2.put("email", "lin2.test2@reqres.in");
        data2.put("first_name", "lin2");
        data2.put("id", "2");
        data2.put("last_name", "test2");

        List<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        jsonList.add(data1);
        jsonList.add(data2);

        HashMap<String, List<HashMap<String, String>>> dataHashMapList = new HashMap<String, List<HashMap<String, String>>>();
        dataHashMapList.put("data", jsonList);

        given(requestSpecification).
                body(dataHashMapList).
        when().
                post().
        then().
                spec(responseSpecification).
//                log().all().
                assertThat().
                statusCode(301);
    }

}