package rs.com.siriusxi.devtech.example.flywayjpa.customer.api;

import com.hazelcast.core.HazelcastInstance;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.service.CustomerService;
import rs.com.siriusxi.devtech.example.flywayjpa.infrastructure.handler.exception.EntityNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

//TODO add xml configuration.
@RestController()
@RequestMapping(
        value = "api"
        //consumes = MediaType.APPLICATION_JSON_VALUE)
        , produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
public class CustomerController {

  private final CustomerService customerService;
  private final RestTemplate restTemplate;
  private final HazelcastInstance instance;

  private final int ttl = 60;


  public CustomerController(CustomerService customerService,
                            RestTemplate restTemplate,
                            HazelcastInstance instance) {

    this.customerService = customerService;
    this.restTemplate = restTemplate;
    this.instance = instance;
  }

  @GetMapping("version")
  @Cacheable("Configs")
  public String versionInformation() {
    return readGitProperties();
  }

  private String readGitProperties() {
    ClassLoader classLoader = getClass().getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream("git.properties")) {
      return readFromInputStream(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
      return "Version information could not be retrieved";
    }
  }

  private String readFromInputStream(InputStream inputStream) throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }

  @GetMapping("health")
  public String health() {
    return "(Service 1) is Up & Running, ".concat(Instant.now().toString());
  }

  @GetMapping("customers/details")
  public List<Customer> getCustomerService() {
    String customerDetails =
            restTemplate.getForObject("http://localhost:8080/Customer-Details/api/details",
                    String.class);

    log.info("Received data from Customer details service {}", customerDetails);

    return customerService.getCustomers();
  }

// Hazelcast Demo
  @GetMapping("customers/add")
  public String addCustomer(@RequestParam("key") String key,
                            @RequestParam("value") String value) {

    // Get map from hazelcast cluster
    instance.getMap("customers")
            /*
               1- Write value, This value will be accessible from another jvm also.
               2- Add a ttl for 60 seconds for each entry for a cache that last forever.
            */
            .put(key, value, ttl, TimeUnit.SECONDS);

    return "Customer:" + value + " has been written to the Cache";
  }

  @GetMapping("customers/{key}")
  public String readCustomer(@PathVariable("key") String key) {
    // get map from hazelcast, also notice it casted to a normal Java Map
    Map<String, String> customersMap = instance.getMap("customers");
    // read customer value from cache first
    return "Customer from Hazelcast cache is :" + Optional.ofNullable(customersMap.get(key))
            // if not present in cache get it from database
            .orElseGet(() ->  customerService
                    .getCustomer(key)
                    //If not present at all throw Entity not found exception
                    .orElseThrow(() -> new EntityNotFoundException(Customer.class,
                            "Key",key))
                    .getName());
  }

  @GetMapping("customers")
  public Map<String, String> getAllCustomers() {
    return instance.getMap("customers");
  }

  // Another Hazelcast config cache
  @GetMapping("config/write")
  public String writeConfig(@RequestParam("values") String values) {
    instance.getMap("Configs")
            .put("data", values);

    return "Configurations values has been written to Hazelcast";
  }

  @GetMapping("config")
  public String readConfig() {
    return "Configurations from Hazelcast is : "
            + instance
            .getMap("Configs")
            .get("data");
  }
}