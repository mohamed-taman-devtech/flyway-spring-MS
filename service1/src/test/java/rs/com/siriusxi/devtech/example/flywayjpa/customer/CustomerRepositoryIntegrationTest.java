package rs.com.siriusxi.devtech.example.flywayjpa.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CustomerRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findCustomerByName() {
        // given
        Customer sirius = new Customer("SiriusXI");
        entityManager.persist(sirius);
        entityManager.flush();

        // when
        Customer found = customerRepository.findByName(sirius.getName());

        // then
        assertEquals(found.getName(), sirius.getName());
        assertEquals(customerRepository.findAll().size(), 6);
    }
}
