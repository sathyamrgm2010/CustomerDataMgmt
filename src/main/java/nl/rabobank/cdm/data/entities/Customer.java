package nl.rabobank.cdm.data.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.NoArgsConstructor;
import nl.rabobank.cdm.data.listeners.CustomerListener;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(CustomerListener.class)
public class Customer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String id;
    String firstName;
    String lastName;
    String address;
    Date dob;
    Integer age;
    Date createdDate;
    Date updatedDate;

    public Customer(String id, String firstName, String lastName, String address, Date dob) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.dob = dob;
	}
}
