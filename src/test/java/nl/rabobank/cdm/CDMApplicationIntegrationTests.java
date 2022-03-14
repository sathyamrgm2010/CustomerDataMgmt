package nl.rabobank.cdm;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.cdm.constants.CDMConstants;
import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.data.repository.CustomerRepository;
import nl.rabobank.cdm.dto.CustomerDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static nl.rabobank.cdm.constants.CDMConstants.NO_RECORD_FOUND;
import static nl.rabobank.cdm.util.CustomerUtil.stringToDate;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { CDMApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
														"spring.datasource.url=jdbc:h2:mem:CustomerDetailsAPITest" })
@AutoConfigureWebTestClient
public class CDMApplicationIntegrationTests {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	public WebTestClient api;

	@Autowired
	public CustomerRepository customerRepo;

	private static final String API_PATH = "/api/customer/";

	@BeforeEach
	public void setUp() {
		customerRepo.deleteAll();
	}

	@Test
	public void test_get_customer_by_id() {
		Customer customer = new Customer(null, "First Name", "Last Name", "Address1", stringToDate("20-12-2000"));
		customerRepo.save(customer);
		api.get().uri(API_PATH + "id/" + customer.getId()).exchange().expectStatus().isOk().expectBody().jsonPath("id")
				.isEqualTo(customer.getId()).jsonPath("firstName").isEqualTo(customer.getFirstName())
				.jsonPath("lastName").isEqualTo(customer.getLastName()).jsonPath("address")
				.isEqualTo(customer.getAddress()).jsonPath("dob").isEqualTo("20-12-2000").jsonPath("age")
				.isEqualTo(customer.getAge());
	}

	@Test
	public void test_get_customer_by_id_not_found() {
		String id = UUID.randomUUID().toString();
		WebTestClient.ResponseSpec responseSpec = api.get().uri(API_PATH + "id/" + id).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.NOT_FOUND, NO_RECORD_FOUND);
	}

	@Test
	public void test_get_customers() {
		Customer customer1 = new Customer(null, "CustomerOneFN", "CustomerOneLN", "Address-1",
											stringToDate("20-12-2000"));
		customerRepo.save(customer1);
		Customer customer2 = new Customer(null, "CustomerTwoFN", "CustomerTwoLN", "Address-2",
											stringToDate("01-01-1999"));
		customerRepo.save(customer2);
		Set<String> expectedIds = Set.of(customer1.getId(), customer2.getId());

		List<CustomerDTO> customerListResponse = api.get().uri(API_PATH).exchange().expectStatus().isOk()
				.expectBodyList(CustomerDTO.class).returnResult().getResponseBody();

		assertNotNull(customerListResponse);

		Set<String> actualIds = customerListResponse.stream().map(customer -> customer.getId())
				    									.collect(Collectors.toSet());
		assertEquals(expectedIds, actualIds);
	}

	@Test
	public void test_get_customers_by_id_not_found() {
		WebTestClient.ResponseSpec responseSpec = api.get().uri(API_PATH).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.NOT_FOUND, NO_RECORD_FOUND);
	}

	@Test
	public void test_get_customer_by_field_name() {
		Customer customer1 = new Customer(null, "CustomerOneFirstName", "CustomerOneLastName", "Address-1",
											stringToDate("20-12-2000"));
		customerRepo.save(customer1);

		Customer customer2 = new Customer(null, "CustomerTwoFirstName", "CustomerTwoLastName", "Address-2",
											stringToDate("01-01-1999"));
		customerRepo.save(customer2);
		Customer customer3 = new Customer(null, "FirstName", "LastName", "Address-2", stringToDate("01-01-1999"));
		customerRepo.save(customer3);

		Set<String> expectedIds = Set.of(customer1.getId(), customer2.getId(), customer3.getId());
		List<CustomerDTO> customerListResponse = api.get()
				.uri(API_PATH + "search?firstName=FirstName&lastName=LastName").exchange().expectStatus().isOk()
				.expectBodyList(CustomerDTO.class).returnResult().getResponseBody();

		assertNotNull(customerListResponse);
		assertEquals(3, customerListResponse.size());

		Set<String> actualIds = customerListResponse.stream().map(customer -> customer.getId())
				.collect(Collectors.toSet());
		assertEquals(expectedIds, actualIds);
	}

	@Test
	public void test_get_customer_by_field_name_not_found() {
		WebTestClient.ResponseSpec responseSpec = api.get()
													.uri(API_PATH + "search?firstName=FirstName&lastName=LastName").exchange();

		verifyErrorResponse(responseSpec, HttpStatus.NOT_FOUND, NO_RECORD_FOUND);
	}

	@Test
	public void test_save_customer() {

		CustomerDTO custDetailReq = new CustomerDTO(null, "FirstName", "LastName", "Address1", "01-01-1999", null, null, null);
		CustomerDTO customerResponse = api.post().uri(API_PATH).contentType(MediaType.APPLICATION_JSON)
											.bodyValue(asJson(custDetailReq)).exchange().expectStatus().isCreated()
											.expectBody(CustomerDTO.class).returnResult().getResponseBody();

		assertNotNull(customerResponse);
		Optional<Customer> createdCustomer = customerRepo.findById(customerResponse.getId());

		assertTrue(createdCustomer.isPresent());
		assertEachField(createdCustomer.get(), custDetailReq);
		assertEquals(23, createdCustomer.get().getAge());
	}

	@Test
	public void test_save_customer_invalid_input() {
		CustomerDTO custDetailReqInvalid = new CustomerDTO(null, "FirstName123$", "LastName123$", "Address1$", "01/01/1999", null, null, null);
		WebTestClient.ResponseSpec responseSpec = api.post().uri(API_PATH).contentType(MediaType.APPLICATION_JSON)
													.bodyValue(asJson(custDetailReqInvalid)).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.BAD_REQUEST, "firstName is invalid, lastName is invalid, address is invalid, dob is invalid");
	}

	@Test
	public void test_save_customer_empty_input() {
		CustomerDTO custDetailReqInvalid = new CustomerDTO(null, "", "", "", "", null, null, null);
		WebTestClient.ResponseSpec responseSpec = api.post().uri(API_PATH).contentType(MediaType.APPLICATION_JSON)
				.bodyValue(asJson(custDetailReqInvalid)).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.BAD_REQUEST, "firstName is mandatory, lastName is mandatory, address is mandatory, dob is mandatory");
	}

	@Test
	public void testU_update_customer() {
		Customer customer = new Customer(null, "First Name", "Last Name", "Address1", stringToDate("20-12-2000"));
		customerRepo.save(customer);
		assertTrue(customerRepo.findById(customer.getId()).isPresent());

		CustomerDTO custDetailReq = new CustomerDTO(null, "NewFirstName", "NewLastName", "NewAddress", "01-01-1999", null, null, null);
		CustomerDTO customerResponse = api.put().uri(API_PATH + "id/" + customer.getId())
											.contentType(MediaType.APPLICATION_JSON).bodyValue(asJson(custDetailReq)).exchange().expectStatus()
											.isOk().expectBody(CustomerDTO.class).returnResult().getResponseBody();

		Optional<Customer> updatedCustomer = customerRepo.findById(customerResponse.getId());
		assertTrue(updatedCustomer.isPresent());
		assertEachField(updatedCustomer.get(), custDetailReq);
	}

	@Test
	public void test_update_customer_invalid_input() {
		Customer customer = new Customer(null, "First Name", "Last Name", "Address1", stringToDate("20-12-2000"));
		customerRepo.save(customer);
		assertTrue(customerRepo.findById(customer.getId()).isPresent());

		CustomerDTO custDetailReqInvalid = new CustomerDTO(null, "FirstName123$", "LastName123$", "Address1$", "01/01/1999", null, null, null);
		WebTestClient.ResponseSpec responseSpec = api.put().uri(API_PATH + "id/" + customer.getId())
													.contentType(MediaType.APPLICATION_JSON).bodyValue(asJson(custDetailReqInvalid)).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.BAD_REQUEST, "firstName is invalid, lastName is invalid, address is invalid, dob is invalid");
	}

	@Test
	public void test_update_customer_empty_input() {
		Customer customer = new Customer(null, "First Name", "Last Name", "Address1", stringToDate("20-12-2000"));
		customerRepo.save(customer);
		assertTrue(customerRepo.findById(customer.getId()).isPresent());

		CustomerDTO custDetailReqInvalid = new CustomerDTO(null, "", "", "", "", null, null, null);
		WebTestClient.ResponseSpec responseSpec = api.put().uri(API_PATH + "id/" + customer.getId())
				.contentType(MediaType.APPLICATION_JSON).bodyValue(asJson(custDetailReqInvalid)).exchange();
		verifyErrorResponse(responseSpec, HttpStatus.BAD_REQUEST, "firstName is mandatory, lastName is mandatory, address is mandatory, dob is mandatory");
	}

	private void assertEachField(Customer savedDetail, CustomerDTO expectedDetail) {
		assertEquals(expectedDetail.getFirstName(), savedDetail.getFirstName());
		assertEquals(expectedDetail.getLastName(), savedDetail.getLastName());
		assertEquals(expectedDetail.getAddress(), savedDetail.getAddress());
		assertEquals(stringToDate(expectedDetail.getDob()), savedDetail.getDob());
	}

	private WebTestClient.BodyContentSpec verifyErrorResponse(WebTestClient.ResponseSpec spec,
															  HttpStatus expectedStatus, String message) {
		return spec.expectStatus().isEqualTo(expectedStatus).expectBody().jsonPath("$.status")
					.isEqualTo(CDMConstants.STATUS_FAILURE).jsonPath("$.message").value(Matchers.containsString(message));
	}

	private String asJson(CustomerDTO value) {
		try {
			return MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException exe) {
			throw new IllegalArgumentException(exe);
		}
	}
}

