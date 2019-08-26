package rs.com.siriusxi.devtech.example.flywayjpa.customer.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.service.CustomerService;

import java.time.Instant;
import java.util.List;

@RestController()
@RequestMapping("customers")
public class CustomerController {

  private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("health")
    public String health(){
        return "(Service 1) is Up & Running, ".concat(Instant.now().toString());
    }

  @GetMapping
  public List<Customer> getCustomerService() {
    return customerService.getCustomers();
  }
}
