package rs.com.siriusxi.devtech.example.flywayjpa.customer.service;

import org.springframework.stereotype.Service;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<Customer> getCustomers() {
        return repo.findAll();
    }

    public Optional<Customer> getCustomer(String key) {
        return repo.findById(Integer.valueOf(key));
    }
}
