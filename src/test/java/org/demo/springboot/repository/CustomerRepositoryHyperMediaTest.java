package org.demo.springboot.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.springboot.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerRepositoryHyperMediaTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Customer> jsonTester;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/springboot-demo" + uri;
    }

//    @Test
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
    public void addNewCustomerTest() throws Exception {
        JacksonTester.initFields(this, objectMapper);
        final String customerJson = jsonTester.write(new Customer()).getJson();
        mockMvc.perform(post(createURLWithPort("/customers"))
                .content(customerJson)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
        Mockito.verify(customerRepository).save(ArgumentMatchers.any(Customer.class));

    }
}
