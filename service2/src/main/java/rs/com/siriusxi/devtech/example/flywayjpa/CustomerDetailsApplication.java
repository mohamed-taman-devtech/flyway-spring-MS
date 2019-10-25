package rs.com.siriusxi.devtech.example.flywayjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CustomerDetailsApplication {

	@GetMapping("api/customer/details")
	public String details() {
		return "Mohamed Mahmoud Taman Sr. Enterprise Architect";
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerDetailsApplication.class, args);
	}
}
