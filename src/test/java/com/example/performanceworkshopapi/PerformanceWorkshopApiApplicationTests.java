package com.example.performanceworkshopapi;

import com.example.performanceworkshopapi.xrftoken.XRFTokenController;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PerformanceWorkshopApiApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private EmployeeController employeeController;
	@Autowired
	private XRFTokenController xrfController;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(employeeController).isNotNull();
		assertThat(xrfController).isNotNull();
	}

	@Test
	void missingXRFHeaderShouldReturnBadRequest() {
		given().when().get("http://localhost:" + port + "/employees/1").then().statusCode(400).body("error", equalTo("Bad Request"));
	}

	@Test
	void wrongXRFHeaderShouldReturnAnuthorised() {
		String resp = given().header("XRF-token","12345").when().get("http://localhost:" + port + "/employees/1").then().statusCode(403).extract().response().getBody().asString();
		assertThat(resp).isEqualTo("Unauthorised");
	}
	@Test
	void correctXRFHeaderShouldReturnEmployee() {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees/1").then().statusCode(200);
	}

	@Test
	void correctXRFShouldReturnAllEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees").then().statusCode(200).body("_embedded.employeeList.size()", greaterThan(1001));
	}

	@Test
	void firstNameShouldFilterEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees?firstName=Frodo").then().statusCode(200).body("_embedded.employeeList", hasSize(1));
	}

	@Test
	void lastNameShouldFilterEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees?lastName=Baggins").then().statusCode(200).body("_embedded.employeeList", hasSize(2));
	}

	@Test
	void employeeCanBeCreated() throws JSONException {
		JSONObject newEmployee = new JSONObject();
		newEmployee.put("firstName", "TestName");
		newEmployee.put("lastName", "TestSurname");
		newEmployee.put("role", "Test Role");
		given().header("XRF-token","1234").header("Content-Type","application/json").body(newEmployee.toString()).when().post("http://localhost:" + port + "/employees").then().statusCode(200);
	}

	@Test
	void employeeCanBeUpdated() throws JSONException {
		JSONObject newEmployee = new JSONObject();
		newEmployee.put("firstName", "TestName");
		newEmployee.put("lastName", "TestSurname");
		newEmployee.put("role", "Test Role");
		String id = given().header("XRF-token","1234").header("Content-Type","application/json")
				.body(newEmployee.toString()).when().post("http://localhost:" + port + "/employees").then().statusCode(200)
				.extract().path("id").toString();

		given().header("XRF-token","1234").header("Content-Type","application/json")
				.body(newEmployee.toString()).when().put("http://localhost:" + port + "/employees/"+id).then().statusCode(201);
	}

	@Test
	void employeeCanBeDeleted() {
		given().header("XRF-token","1234").when().delete("http://localhost:" + port + "/employees/1").then().statusCode(204);
	}

	@Test
	void userCanCreateXRFToken() {
		given().when().post("http://localhost:" + port + "/xrf-token").then().statusCode(200).body("token", hasLength(36));
	}

	@Test
	void notFoundEmployeeShouldGive404 () {
		String body = given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees/987456321").then().statusCode(404).extract().response().getBody().asString();
		assertEquals("Could not find employee 987456321", body);
	}

	@Test
	void putNewEmployeeShouldReturn201() throws JSONException {
		JSONObject newEmployee = new JSONObject();
		newEmployee.put("firstName", "TestName");
		newEmployee.put("lastName", "TestSurname");
		newEmployee.put("role", "Test Role");
		given().header("XRF-token","1234").header("Content-Type","application/json")
				.body(newEmployee.toString()).when().put("http://localhost:" + port + "/employees/888888").then().statusCode(201);

	}
}
