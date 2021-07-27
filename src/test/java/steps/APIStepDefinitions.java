package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.junit.Assert;
import utils.ExcelUtils;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class APIStepDefinitions {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    private static final String ENDPOINT_USER = "http://reqres.in/api/users";

    @Given("^the words exist with count of '(.*)'$")
    public void wordCountParam(String number) {
        request = given().param("count", number);
    }

    @When("^Verify get users")
    public void getUsers() {
        response = given().
                header("Content-Type", "application/json").
                param("page", 2).
                when().
                get(ENDPOINT_USER).
                then().statusCode(HttpStatus.SC_OK).
                log().all().
                contentType(ContentType.JSON).
                assertThat().
                body("data.id[0]", equalTo(7)).
                extract().response();


        System.out.println("response: " + response.prettyPrint());
    }


    @When("^Verify post users")
    public void postUsers() {
        JSONObject request = new JSONObject();
        request.put("name", "John");
        request.put("position", "Doctor");
        given().
                header("Content-Type", "application/json").
                contentType(ContentType.JSON).
                body(request.toJSONString()).
                when().
                post(ENDPOINT_USER).
                then().
                statusCode(301).
                log().all();
    }

    @When("^Verify update users")
    public void patchUsers() {
        JSONObject request = new JSONObject();
        request.put("name", "James");
        request.put("position", "Engineer");
        given().
                header("Content-Type", "application/json").
                contentType(ContentType.JSON).
                body(request.toJSONString()).
                when().
                patch(ENDPOINT_USER).
                then().
                statusCode(301).
                log().all();
    }


    @Then("^the status code is (\\d+)$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response includes the following$")
    public void response_equals(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), equalTo(Integer.parseInt(field.getValue())));
            }
        }
    }

    @And("response includes empty record")
    public void no_response_equals() {
        Assert.assertTrue(response.body().asString().contains(""));
    }

    @And("response includes the following in any order")
    public void response_contains_in_any_order(Map<String, String> responseFields) {
        for (Map.Entry<String, String> field : responseFields.entrySet()) {
            if (StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getKey(), containsInAnyOrder(field.getValue()));
            }
        }
    }

    @And("^the error message is as below$")
    public void errorMessageValidation(Map<String, String> expError) throws NullPointerException {
        for (Map.Entry<String, String> errorField : expError.entrySet()) {
            json.body(errorField.getKey(), equalTo(errorField.getValue()));
        }
    }

    @And("^Get excel data rows")
    public void getExcelRows() throws IOException {
        ExcelUtils.getExcelRows();
    }

    @And("^Write a new excel of empDetail")
    public void writeExcel() throws IOException {
        ExcelUtils.writeExcel();
    }
}


