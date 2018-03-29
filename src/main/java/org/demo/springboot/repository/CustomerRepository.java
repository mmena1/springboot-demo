package org.demo.springboot.repository;

import org.demo.springboot.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(@Param("lastName") String lastName);

    @Query("select c from Customer c where c.firstName like ?1%")
    List<Customer> findByFirstNameStartsWith(@Param("firstName") String firstName);
}
