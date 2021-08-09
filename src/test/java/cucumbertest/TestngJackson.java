package cucumbertest;

import io.cucumber.java.hu.Ha;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class TestngJackson {

    private static final String MOCK_HOST_URL = "https://c0ee27a3-1e52-4441-9de3-345e3cea65ac.mock.pstmn.io";

    ResponseSpecification responseSpecification;

    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(MOCK_HOST_URL).addHeader("Content-Type", "application/json").
                addHeader("x-mock-match-request-body", "true").
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void validatePostPayloadJsonArrayAsList() {
        HashMap<String, String> obj5003 = new HashMap<String, String>();
        obj5003.put("id", "5003");
        obj5003.put("type", "none");

        HashMap<String, String> obj5004 = new HashMap<String, String>();
        obj5004.put("id", "5004");
        obj5004.put("type", "glazed");

        List<HashMap> jsonList = new ArrayList<HashMap>();
        jsonList.add(obj5003);
        jsonList.add(obj5004);

        HashMap<String, List<HashMap>> toppingHasHMap = new HashMap<String, List<HashMap>>();
        toppingHasHMap.put("topping", jsonList);

        given().
                body(toppingHasHMap).
        when().
                post("/postComplexJson").
        then().spec(responseSpecification).
                log().all().
                assertThat().
                body("message", equalTo("Successful"));
    }
}
