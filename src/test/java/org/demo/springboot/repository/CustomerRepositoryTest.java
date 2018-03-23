package org.demo.springboot.repository;

import org.demo.springboot.entity.Customer;
import org.assertj.core.api.Condition;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        customerRepository.save(new Customer("Jack", "Bauer"));
        customerRepository.save(new Customer("Kim", "Bauer"));
        customerRepository.save(new Customer("David", "Palmer"));
    }

    @Test
    public void findByLastNameTest() {
        List<Customer> customers = customerRepository.findByLastName("Bauer");
        List<String> bauers = Lists.newArrayList("Jack", "Kim");
        Condition<String> bauerLastname = new Condition<>(bauers::contains, "bauer lastname");
        assertThat(customers.size()).isEqualTo(2);
        customers.forEach(customer -> {
            assertThat(customer.getFirstName()).has(bauerLastname);
        });
    }

    @Test
    public void findByFirstNameStartsWith() {
        List<Customer> customers = customerRepository.findByFirstNameStartsWith("Dav");
        assertThat(customers.size()).isEqualTo(1);
        customers.forEach(customer -> assertThat(customer.getFirstName()).isEqualTo("David"));
    }
}
