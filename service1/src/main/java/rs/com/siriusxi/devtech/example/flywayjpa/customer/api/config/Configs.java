package rs.com.siriusxi.devtech.example.flywayjpa.customer.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Configs {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
