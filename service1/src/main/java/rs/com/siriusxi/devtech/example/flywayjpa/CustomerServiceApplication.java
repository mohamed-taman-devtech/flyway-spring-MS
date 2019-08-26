package rs.com.siriusxi.devtech.example.flywayjpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import rs.com.siriusxi.devtech.example.flywayjpa.customer.repository.CustomerRepository;

@SpringBootApplication
@Slf4j
public class CustomerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerServiceApplication.class, args);
  }

  @Bean
  public CommandLineRunner runner(CustomerRepository repository) {
    return r -> log.info(repository.findAll().toString());
  }
}
