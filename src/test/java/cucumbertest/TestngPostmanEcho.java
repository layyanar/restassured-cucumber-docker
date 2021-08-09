package cucumbertest;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import org.testng.annotations.Test;

import java.io.*;
import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class TestngPostmanEcho {

    @Test
    public void queryParameter() { //Using json-schema-validator //Using filter to print log that you need
        given().
                baseUri("https://postman-echo.com").
                queryParam("foo1", "bar1").
                filter(new RequestLoggingFilter(LogDetail.BODY)).
                filter(new RequestLoggingFilter(LogDetail.STATUS)).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200).
                body(matchesJsonSchemaInClasspath("schema/pmEchoGet.json")); //json-schema-validator
    }

    @Test
    public void multiQueryParameter() {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("foo1", "bar1");
        queryParams.put("foo2", "bar2");
        given().
                baseUri("https://postman-echo.com").
//                queryParam("foo1", "bar1").
//                queryParam("foo2", "bar2").
                queryParams(queryParams).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multiQueryParamWithMultiValue() {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("foo1", "bar1");
        queryParams.put("foo1", "bar1");
        queryParams.put("foo2", "bar2");

        given().
                baseUri("https://postman-echo.com").
//                queryParam("foo1", "bar1, bar2, bar3").
//                queryParam("foo1", "bar1;bar2;bar3").
                queryParams(queryParams).
        when().
                get("/get").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multiFormData() {
        given().
                baseUri("https://postman-echo.com").
                multiPart("foo1", "bar1").
                multiPart("foo2", "bar2").
                log().all().
        when().
                post("/post").
        then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void multiPartUploadFile() {
        String attribute = "{\"name\":\"temp.txt\",\"parent\":{\"id\":\"12345\"}}";
        given().
                baseUri("https://postman-echo.com").
                multiPart("file", new File("temp.txt")).
                multiPart("attributes", attribute, "application/json").
                log().all().
       when().
                post("/post").
       then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void downloadFile() throws IOException {
        byte[] bytes =  given().
                                baseUri("https://raw.githubusercontent.com").
                                log().all().
                        when().
                                get("/appium/appium/master/sample-code/apps/ApiDemos-debug.apk").
                        then().
                                log().all().
                                extract().
                                response().asByteArray();

        OutputStream os = new FileOutputStream(new File("ApiDemos-debug.apk"));
        os.write(bytes);
        os.close();
    }
}
