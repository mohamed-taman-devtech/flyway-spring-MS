package rs.com.siriusxi.devtech.example.flywayjpa.customer;

import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.domain.Customer;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.repository.CustomerRepository;

import javax.persistence.Entity;
import javax.persistence.Query;

import java.util.Date;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Test find customer by name.")
    @Order(1)
    void findCustomerByName() {

        printMaxConValue("After");

        // given
        Customer sirius = new Customer("SiriusXI");
        entityManager.persist(sirius);
        entityManager.flush();

        // when
        Customer found = customerRepository.findByName(sirius.getName());

        // then
        assertEquals(found.getName(), sirius.getName());
    }

    @Test
    @DisplayName("Test find All customers.")
    @Order(2)
    void findAllCustomer() {
        assertEquals(customerRepository.findAll().size(), 5);
    }




    // TODO move to a helper classes
    // Try to set some database parameters but fix was in Flyway
    private void setMaxConnections(int value){

        //Increase max number of connections for test purpose
        entityManager
                .getEntityManager()
                .createNativeQuery("SET GLOBAL MAX_CONNECTIONS = :value")
                .setParameter("value",value)
                .executeUpdate();

        out.println("max connections increased to "+value);
    }

    private void printMaxConValue(String tag){

        Object[] variables = (Object[])entityManager
                .getEntityManager()
                .createNativeQuery("SHOW VARIABLES LIKE 'MAX_CONNECTIONS%'").getSingleResult();

        out.printf("%s: [variable_name:%s, value:%s] %n",tag,variables[0],variables[1]);

    }
}
