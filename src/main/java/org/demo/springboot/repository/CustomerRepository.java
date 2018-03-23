package org.demo.springboot.repository;

import org.demo.springboot.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Query("select c from Customer c where c.firstName like ?1%")
    List<Customer> findByFirstNameStartsWith(String firstName);
}
