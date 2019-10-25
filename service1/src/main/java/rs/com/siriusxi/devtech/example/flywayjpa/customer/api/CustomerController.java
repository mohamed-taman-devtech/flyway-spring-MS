package rs.com.siriusxi.devtech.example.flywayjpa.customer.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.service.CustomerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

@RestController()
@RequestMapping("api/customers")
@Log4j2
public class CustomerController {

  private final CustomerService customerService;
  private final RestTemplate restTemplate;

  public CustomerController(CustomerService customerService, RestTemplate restTemplate) {
    this.customerService = customerService;
    this.restTemplate = restTemplate;
  }

  @GetMapping("version")
  @Cacheable("version")
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

  @GetMapping
  public List<Customer> getCustomerService() {
    String customerDetails =
        restTemplate.getForObject("http://localhost:8080/api/customer" + "/details", String.class);

    log.info("Received data from Customer details service {}", customerDetails);

    return customerService.getCustomers();
  }
}
