package nl.rabobank.cdm.service;

import static nl.rabobank.cdm.constants.CDMConstants.CUSTOMER_SAVE_OPERATION_FAILED;
import static nl.rabobank.cdm.constants.CDMConstants.CUSTOMER_UPDATE_OPERATION_FAILED;
import static nl.rabobank.cdm.constants.CDMConstants.NO_RECORD_FOUND;
import static nl.rabobank.cdm.util.CustomerUtil.stringToDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import nl.rabobank.cdm.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.data.repository.CustomerRepository;
import nl.rabobank.cdm.exception.CDMApiException;
import nl.rabobank.cdm.exception.CDMValidationException;
import nl.rabobank.cdm.dto.CustomerDTO;
import nl.rabobank.cdm.validator.CustomerValidator;


public class CustomerServiceTest {
	
	@InjectMocks
	CustomerServiceImpl customerService;
	
	@Mock
	CustomerValidator customerValidator;
	
	@Mock
	CustomerRepository customerRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		customerService.setModelMapper(TestUtil.getModelMapper());
	}
	
	@Test
	public void test_get_all_customers_happy() throws CDMApiException {
		List<Customer> expectedCustomerList = new ArrayList<>();
		expectedCustomerList.add(new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date()));
		expectedCustomerList.add(new Customer("123e4567-e89b-12d3-a456-426614174001", "testuser2", "lastname2", "address2", new Date()));
		when(customerRepository.findAll()).thenReturn(expectedCustomerList);
		List<Customer> actualCustomerList = customerService.getCustomers();
		verify(customerRepository).findAll();
		assertEquals(expectedCustomerList, actualCustomerList);
		
	}
	
	@Test
	public void test_get_all_customers_empty() throws CDMApiException {
		when(customerRepository.findAll()).thenReturn(new ArrayList<Customer>());
		CDMApiException cdmApiException = null;
		try {
			customerService.getCustomers();
		}
		catch(CDMApiException e) {
			cdmApiException = e;
		}
		verify(customerRepository).findAll();
		assertEquals(NO_RECORD_FOUND, cdmApiException.getMessage());
		
	}
	
	@Test
	public void test_get_customer_by_id_happy() throws CDMApiException {
		Customer expectedCustomer= new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", stringToDate("01-01-2000"));
		Optional<Customer> expectedCustomerOpt = Optional.of(expectedCustomer);
		when(customerRepository.findById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(expectedCustomerOpt);
		Customer actualCustomer = customerService.getCustomerById("123e4567-e89b-12d3-a456-426614174000");
		verify(customerRepository).findById(any());
		assertEquals(expectedCustomer, actualCustomer);
		
	}
	
	@Test
	public void test_get_customer_by_id_empty() throws CDMApiException {
		Optional<Customer> expectedCustomer = Optional.empty();
		CDMApiException cdmApiException = null;
		when(customerRepository.findById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(expectedCustomer);
		try {
			customerService.getCustomerById("123e4567-e89b-12d3-a456-426614174000");
		}
		catch(CDMApiException e) {
			cdmApiException = e;
		}
		verify(customerRepository).findById(any());
		assertEquals(NO_RECORD_FOUND, cdmApiException.getMessage());
		
	}
	
	@Test
	public void test_get_customer_by_fieldName_happy() throws CDMApiException {
		List<Customer> expectedCustomerList = new ArrayList<>();
		expectedCustomerList.add(new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date()));
		when(customerRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("testuser1", "lastname1")).thenReturn(expectedCustomerList);
		List<Customer> actualCustomerList = customerService.getCustomerByFieldName("testuser1", "lastname1");
		verify(customerRepository).findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(any(), any());
		assertEquals(expectedCustomerList, actualCustomerList);
		
	}
	
	@Test
	public void test_get_customer_by_fieldName_by_first_name() throws CDMApiException {
		List<Customer> expectedCustomerList = new ArrayList<>();
		expectedCustomerList.add(new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date()));
		when(customerRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("testuser1", "")).thenReturn(expectedCustomerList);
		List<Customer> actualCustomerList = customerService.getCustomerByFieldName("testuser1", "");
		verify(customerRepository).findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(any(), any());
		assertEquals(expectedCustomerList, actualCustomerList);
		
	}
	
	@Test
	public void test_get_customer_by_fieldName_by_last_name() throws CDMApiException {
		List<Customer> expectedCustomerList = new ArrayList<>();
		expectedCustomerList.add(new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date()));
		when(customerRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("", "lastname1")).thenReturn(expectedCustomerList);
		List<Customer> actualCustomerList = customerService.getCustomerByFieldName("", "lastname1");
		verify(customerRepository).findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(any(), any());
		assertEquals(expectedCustomerList, actualCustomerList);
		
	}
	
	@Test
	public void test_get_customer_by_fieldName_empty() throws CDMApiException {
		CDMApiException cdmApiException = null;
		try {
			customerService.getCustomerByFieldName("testuser1", "lastname1");
		}
		catch(CDMApiException e) {
			cdmApiException = e;
		}
		assertEquals(NO_RECORD_FOUND, cdmApiException.getMessage());
	}
	
	@Test
	public void test_save_valid_customer() throws CDMApiException {
		Customer customer = new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date());
		when(customerRepository.save(customer)).thenReturn(customer);
		CDMApiException cdmApiException = null;
		try {
			customerService.saveCustomer(new CustomerDTO("1", "testuser1", "lastname1", "address1", "01-01-2000", null, null, null));
		}
		catch(CDMApiException ex) {
			cdmApiException = ex;
		}
		verify(customerRepository).save(any());
		assertNull(cdmApiException);
	}
	
	@Test
	public void test_save_invalid_customer() throws CDMApiException {
		CDMValidationException cdmValidationException = null;
		try {
			Mockito.doThrow(new CDMValidationException("id is invalid")).when(customerValidator).validateCustomerDetails(any());
			customerService.saveCustomer(new CustomerDTO("1", "testuser1", "lastname1", "address1", "01-01-2000", null,null, null));
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		
		assertNotNull(cdmValidationException);
		assertEquals("id is invalid", cdmValidationException.getMessage());
	}
	
	@Test
	public void test_save_customer_failed() throws CDMApiException {
		when(customerRepository.save(any())).thenThrow(new IllegalArgumentException("invalid"));
		CDMApiException cdmApiException = null;
		try {
			customerService.saveCustomer(new CustomerDTO("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", "01-01-2000", null,null, null));
		}
		catch(CDMApiException ex) {
			cdmApiException = ex;
		}
		verify(customerRepository).save(any());
		assertNotNull(cdmApiException);
		assertEquals(CUSTOMER_SAVE_OPERATION_FAILED, cdmApiException.getMessage());
	}

	@Test
	public void test_update_valid_customer() throws CDMApiException {
		Customer customer = new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", new Date());
		Optional<Customer> expectedCustomerOpt = Optional.of(customer);
		when(customerRepository.findById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(expectedCustomerOpt);
		when(customerRepository.save(customer)).thenReturn(customer);
		CDMApiException cdmApiException = null;
		try {
			customerService.updateCustomer("123e4567-e89b-12d3-a456-426614174000", new CustomerDTO(null, "testuser1", "lastname1", "address1", "01-01-2000", null,null, null));
		}
		catch(CDMApiException ex) {
			cdmApiException = ex;
		}
		verify(customerRepository).findById("123e4567-e89b-12d3-a456-426614174000");
		verify(customerRepository).save(any());
		assertNull(cdmApiException);
	}
	
	@Test
	public void test_update_invalid_customer() throws CDMApiException {
		CDMValidationException cdmValidationException = null;
		try {
			Mockito.doThrow(new CDMValidationException("firstName is invalid")).when(customerValidator).validateCustomerDetails(any());
			customerService.updateCustomer("123e4567-e89b-12d3-a456-426614174000", new CustomerDTO(null, "testuser1$", "lastname1", "address1", "01-01-2000", null,null, null));
		}
		catch(CDMValidationException ex) {
			cdmValidationException = ex;
		}
		
		assertNotNull(cdmValidationException);
		assertEquals("firstName is invalid", cdmValidationException.getMessage());
	}
	
	@Test
	public void test_update_customer_not_found() throws CDMApiException {
		Optional<Customer> expectedCustomerOpt = Optional.empty();
		when(customerRepository.findById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(expectedCustomerOpt);
		CDMApiException cdmApiException = null;
		try {
			customerService.updateCustomer("123e4567-e89b-12d3-a456-426614174000", new CustomerDTO(null, "testuser1", "lastname1", "address1", "01-01-2000", null,null, null));
		}
		catch(CDMApiException ex) {
			cdmApiException = ex;
		}
		verify(customerRepository).findById("123e4567-e89b-12d3-a456-426614174000");
		assertNotNull(cdmApiException);
		assertEquals(NO_RECORD_FOUND, cdmApiException.getMessage());
	}
	
	@Test
	public void test_update_customer_failed() throws CDMApiException {
		Customer expectedCustomer= new Customer("123e4567-e89b-12d3-a456-426614174000", "testuser1", "lastname1", "address1", stringToDate("01-01-2000"));
		Optional<Customer> expectedCustomerOpt = Optional.of(expectedCustomer);
		when(customerRepository.findById(any())).thenReturn(expectedCustomerOpt);
		when(customerRepository.save(any())).thenThrow(new IllegalArgumentException("invalid"));
		CDMApiException cdmApiException = null;
		try {
			customerService.updateCustomer("123e4567-e89b-12d3-a456-426614174000", new CustomerDTO(null, "testuser1", "lastname1", "address1", "01-01-2000", null, null, null));
		}
		catch(CDMApiException ex) {
			cdmApiException = ex;
		}
		verify(customerRepository).findById(any());
		verify(customerRepository).save(any());
		assertNotNull(cdmApiException);
		assertEquals(CUSTOMER_UPDATE_OPERATION_FAILED, cdmApiException.getMessage());
	}
	
}
