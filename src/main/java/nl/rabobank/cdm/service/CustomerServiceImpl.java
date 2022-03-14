package nl.rabobank.cdm.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.data.repository.CustomerRepository;
import nl.rabobank.cdm.dto.CustomerDTO;
import nl.rabobank.cdm.exception.CDMApiException;
import nl.rabobank.cdm.validator.CustomerValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static nl.rabobank.cdm.constants.CDMConstants.*;


@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

	private final CustomerValidator customerValidator;
	private final CustomerRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Gets all the customer details.
	 * @return List of Customer.
	 * @throws CDMApiException
	 */
	@Transactional(readOnly = true)
	public List<Customer> getCustomers() throws CDMApiException {
		List<Customer> customerList = new ArrayList<>();
		Iterable<Customer> customerIterator = customerRepository.findAll();
		customerIterator.forEach(customerList::add);
		
		if (customerList.size() == 0) {
			log.error("No customer data found in the database.");
			throw new CDMApiException(NO_RECORD_FOUND);
		}
        return customerList;
    }

	/**
	 * Gets the customer detail for the given customer id.
	 * @param id
	 * @return
	 * @throws CDMApiException
	 */
	@Transactional(readOnly = true)
	 public Customer getCustomerById(String id) throws CDMApiException {
		 Optional<Customer> customer = customerRepository.findById(id);
	     if (customer.isEmpty()) {
			 log.error("No record is found for the customer id : " + id);
	    	 throw new CDMApiException(NO_RECORD_FOUND);
	     }
	     return customer.get();
	 }

	/**
	 * Search customers by matching the given firstName and lastName.
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws CDMApiException
	 */
	@Transactional(readOnly = true)
    public List<Customer> getCustomerByFieldName(String firstName, String lastName) throws CDMApiException {
    	List<Customer> customerList = null;
    	customerList = customerRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        if (customerList == null || customerList.size() == 0) {
			log.error("No search result found for the firstName : " + firstName + " and lastName : " + lastName);
        	throw new CDMApiException(NO_RECORD_FOUND);
        }
        return customerList;
    }

	/**
	 * Save a new customer details.
	 * @param customerDTO
	 * @return
	 * @throws CDMApiException
	 */
	@Transactional
    public Customer saveCustomer(CustomerDTO customerDTO) throws CDMApiException{
    	Customer customer = null;
    	customerValidator.validateCustomerDetails(customerDTO);
    	try {
    		customerDTO.setId(null); //clear id (if present) before save
			customer = modelMapper.map(customerDTO, Customer.class);
    		customer = customerRepository.save(customer);
    	}
    	catch(IllegalArgumentException ex) {
			log.error("Exception occurred while saving/creating the customer data.", ex);
    		throw new CDMApiException(CUSTOMER_SAVE_OPERATION_FAILED);
    	}
    	return customer;
    }

	/**
	 * Updates an existing customer details.
	 * @param id
	 * @param customerDTO
	 * @return
	 * @throws CDMApiException
	 */
	@Transactional
    public Customer updateCustomer(String id, CustomerDTO customerDTO) throws CDMApiException {
    	Customer customer = null;
    	customerValidator.validateCustomerDetails(customerDTO);
		customerDTO.setId(id);
    	try {
    		Customer retrievedCustomer = getCustomerById(id); //check if the customer already exists
    		if (retrievedCustomer != null) {
				customer = modelMapper.map(customerDTO, Customer.class);
				customer.setCreatedDate(retrievedCustomer.getCreatedDate());
    			customer = customerRepository.save(customer);
    		}
    	}
    	catch(IllegalArgumentException ex) {
			log.error("Exception occurred while updating the customer data", ex);
    		throw new CDMApiException(CUSTOMER_UPDATE_OPERATION_FAILED);
    	}
    	return customer;
    }
}
