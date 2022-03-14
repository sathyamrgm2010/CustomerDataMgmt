package nl.rabobank.cdm.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import nl.rabobank.cdm.data.entities.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

	public List<Customer> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);
	
}
