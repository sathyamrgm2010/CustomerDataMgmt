package nl.rabobank.cdm.service;

import java.util.List;

import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.exception.CDMApiException;
import nl.rabobank.cdm.dto.CustomerDTO;

public interface CustomerService {
	
	public List<Customer> getCustomers() throws CDMApiException;

    public Customer getCustomerById(String id) throws CDMApiException;
	 
    public List<Customer> getCustomerByFieldName(String firstName, String lastName) throws CDMApiException;
    
    public Customer saveCustomer(CustomerDTO customerDTO) throws CDMApiException;

    public Customer updateCustomer(String id, CustomerDTO customerDTO) throws CDMApiException;

}
