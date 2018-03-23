package org.demo.springboot.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.springboot.entity.Customer;
import org.demo.springboot.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer("Jack", "Bauer"));
        customerList.add(new Customer("Kim", "Bauer"));
        customerList.add(new Customer("David", "Palmer"));

        Mockito.when(customerRepository.findAll()).thenReturn(customerList);
        restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/customers/all"), String.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        JsonNode root = objectMapper.readTree(response.getBody());
        Customer[] customerArray = objectMapper.treeToValue(root, Customer[].class);
        assertThat(customerArray.length).isEqualTo(3);
        assertThat(customerArray[0].getFirstName()).isEqualTo("Jack");
        assertThat(customerArray[1].getFirstName()).isEqualTo("Kim");
        assertThat(customerArray[2].getFirstName()).isEqualTo("David");
    }

    @Test
    public void addNewCustomerTest() {
        restTemplate = new TestRestTemplate();
        List<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer("Martin", "Mena"));

        Mockito.when(customerRepository.save(customerList.get(0))).thenReturn(customerList.get(0));
        ResponseEntity<String> response = restTemplate.getForEntity( createURLWithPort("/customers/add?firstName=Martin&lastName=Mena"), String.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Saved");

        Mockito.when(customerRepository.findAll()).thenReturn(customerList);
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        assertThat(customers.size()).isEqualTo(1);
        assertThat(customers.get(0).getFirstName()).isEqualTo("Martin");
    }
}
