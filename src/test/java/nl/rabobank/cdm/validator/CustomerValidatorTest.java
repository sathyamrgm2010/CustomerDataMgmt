package nl.rabobank.cdm.validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import nl.rabobank.cdm.exception.CDMValidationException;
import nl.rabobank.cdm.dto.CustomerDTO;

public class CustomerValidatorTest {
	
	@InjectMocks
	CustomerValidator customerValidator;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void test_valid_customer_details() {
		CustomerDTO customerDTO = new CustomerDTO("123e4567-e89b-12d3-a456-426614174000", "Srini", "Venkat", "address1", "01-01-2000", "22", "01-04-2000 12:00:00", "01-01-2022 11:00:00");
		CDMValidationException cdmValidationException = null;
		try {
			customerValidator.validateCustomerDetails(customerDTO);
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		assertNull(cdmValidationException);
		
	}

	@Test
	public void test_invalid_values() {
		CustomerDTO customerDTO = new CustomerDTO("", "testuser#", "lastname#", "address()1", "01-01-2000", "22", "01-04-2000 12:00:00", "01-01-2022 11:00:00");
		CDMValidationException cdmValidationException = null;
		try {
			customerValidator.validateCustomerDetails(customerDTO);
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		assertNotNull(cdmValidationException);
		
	}

	@Test
	public void test_empty_values() {
		CustomerDTO customerDTO = new CustomerDTO(null, "", "lastname", "address1", "01-01-2000", "22", "01-04-2000 12:00:00", "01-01-2022 11:00:00");
		CDMValidationException cdmValidationException = null;
		try {
			customerValidator.validateCustomerDetails(customerDTO);
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		assertNotNull(cdmValidationException);
	}
	
	@Test
	public void test_invalid_date() {
		CustomerDTO customerDTO = new CustomerDTO("123e4567-e89b-12d3-a456-426614174000", "testuser", "lastname", "address1", "30-02-2000", "21",  "01-04-2000 12:00:00", "01-01-2022 11:00:00");
		CDMValidationException cdmValidationException = null;
		try {
			customerValidator.validateCustomerDetails(customerDTO);
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		assertNotNull(cdmValidationException);
	}

	@Test
	public void test_date_greater_than_today() {
		CustomerDTO customerDTO = new CustomerDTO("123e4567-e89b-12d3-a456-426614174000", "Srini", "Venkat", "address1", "01-01-2023", "22", "01-04-2000 12:00:00", "01-01-2022 11:00:00");
		CDMValidationException cdmValidationException = null;
		try {
			customerValidator.validateCustomerDetails(customerDTO);
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		assertNotNull(cdmValidationException);
	}

}
