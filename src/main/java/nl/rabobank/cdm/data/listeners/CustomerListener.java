package nl.rabobank.cdm.data.listeners;

import nl.rabobank.cdm.data.entities.Customer;
import nl.rabobank.cdm.util.CustomerUtil;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class CustomerListener {

    @PrePersist
    private  void beforeCreate(Customer customer) {
        customer.setCreatedDate(new Date());
        customer.setAge(CustomerUtil.calculateAge(customer.getDob()));
    }

    @PreUpdate
    private void beforeUpdate(Customer customer) {
        customer.setUpdatedDate(new Date());
        customer.setAge(CustomerUtil.calculateAge(customer.getDob()));
    }
}
