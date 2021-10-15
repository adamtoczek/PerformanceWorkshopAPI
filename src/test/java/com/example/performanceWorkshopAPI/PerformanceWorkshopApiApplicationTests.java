package com.example.performanceWorkshopAPI;

import com.example.performanceWorkshopAPI.xrfToken.XRFTokenController;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


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
	public void missingXRFHeaderShouldReturnBadRequest() {
		given().when().get("http://localhost:" + port + "/employees/1").then().statusCode(400).body("error", equalTo("Bad Request"));
	}

	@Test
	public void wrongXRFHeaderShouldReturnAnuthorised() {
		String resp = given().header("XRF-token","12345").when().get("http://localhost:" + port + "/employees/1").then().statusCode(403).extract().response().getBody().asString();
		assertThat(resp).isEqualTo("Unauthorised");
	}
	@Test
	public void correctXRFHeaderShouldReturnEmployee() {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees/1").then().statusCode(200);
	}

	@Test
	public void correctXRFShouldReturnAllEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees").then().statusCode(200).body("_embedded.employeeList", hasSize(1002));
	}

	@Test
	public void firstNameShouldFilterEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees?firstName=Frodo").then().statusCode(200).body("_embedded.employeeList", hasSize(1));
	}

	@Test
	public void lastNameShouldFilterEmployees () {
		given().header("XRF-token","1234").when().get("http://localhost:" + port + "/employees?lastName=Baggins").then().statusCode(200).body("_embedded.employeeList", hasSize(2));
	}

	@Test
	public void employeeCanBeCreated() throws JSONException {
		JSONObject newEmployee = new JSONObject();
		newEmployee.put("firstName", "TestName");
		newEmployee.put("lastName", "TestSurname");
		newEmployee.put("role", "Test Role");
		given().header("XRF-token","1234").header("Content-Type","application/json").body(newEmployee.toString()).when().post("http://localhost:" + port + "/employees").then().statusCode(200);
	}

	@Test
	public void employeeCanBeUpdated() throws JSONException {
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
	public void employeeCanBeDeleted() {
		given().header("XRF-token","1234").when().delete("http://localhost:" + port + "/employees/1").then().statusCode(204);
	}

}
