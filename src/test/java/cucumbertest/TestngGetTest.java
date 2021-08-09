package cucumbertest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestngGetTest {

    private static final String ENDPOINT_USER = "http://reqres.in";

    @Test //using testng and hamcrest asserts
    public void test() {

        Set<String> headers = new HashSet<String>();
        headers.add("Content-Type");
        headers.add("Accept");
        Response res = given().header("Content-Type", "application/json").
                               param("page", 2).
                               config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))). //Way to blacklist the collection of headers
                               log().all().
                        when().get(ENDPOINT_USER+"/api/users").
                        then().statusCode(HttpStatus.SC_OK).
                                log().all().
                                contentType(ContentType.JSON).
                                body("data.id[0]", is(equalTo(7)),
                                        "data.size()", equalTo(6), //check size in one way
                                        "data", hasSize(6), //check size in one way
                                        "data.id", hasItems(7, 8, 9, 10, 11, 12), //check size in another way
                                        "data[0]", hasEntry("email", "michael.lawson@reqres.in"), //check map entry
                                        "data[0]", not(equalTo(Collections.EMPTY_MAP))). //Check if collection is not empty
                                extract().response();

        //Using Testng assert
        Assert.assertEquals(res.path("data[0].first_name"), "Michael");

        //Using Hamcrest assert
        assertThat(res.path("data[0].first_name"), equalTo("Michael"));

        //print entire response body
        System.out.println("response is " +res.asString());

        //Use path & print name from first data array of Response body
        System.out.println("first_name = " + res.path("data[0].first_name"));

        //Use JsonPath object to print name from first data array of Response body
        JsonPath jsonPath = new JsonPath(res.asString());
        System.out.println("first_name = " + jsonPath.getString("data[0].first_name"));

        //Use JsonPath.from to print name from first data array of Response body
        System.out.println("first_name = " + JsonPath.from(res.asString()).getString("data[0].first_name"));

    }

    @Test
    public void pathParameter() throws FileNotFoundException {
        PrintStream fileOutputStream = new PrintStream(new File("restAssuredGet.log"));

        given().
                baseUri(ENDPOINT_USER).
                pathParam("userId", "2").
                log().all().
//                filter(new RequestLoggingFilter(fileOutputStream)). //logs all details about request
//                filter(new ResponseLoggingFilter(fileOutputStream)). //logs all details about response
                filter(new RequestLoggingFilter(LogDetail.BODY, fileOutputStream)). //logs all details about request
                filter(new ResponseLoggingFilter(LogDetail.STATUS, fileOutputStream)). //logs all details about response
        when().
                get("/api/users/{userId}").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

}
