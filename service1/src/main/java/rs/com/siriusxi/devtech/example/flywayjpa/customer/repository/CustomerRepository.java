package rs.com.siriusxi.devtech.example.flywayjpa.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    public Customer findByName(String name);
}
