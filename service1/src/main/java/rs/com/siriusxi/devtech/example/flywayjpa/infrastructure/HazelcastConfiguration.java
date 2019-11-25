package rs.com.siriusxi.devtech.example.flywayjpa.infrastructure;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.hazelcast.config.EvictionPolicy.*;
import static com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE;

@Configuration
public class HazelcastConfiguration {

    @Bean
    public Config hazelCastConfig() {
        return new Config()
                //Set service instance name
                .setInstanceName("customerService-cache-instance")
                .addMapConfig(
                        new MapConfig() // create map
                                .setName("customers")
                                .setMaxSizeConfig(new MaxSizeConfig(200,
                                        FREE_HEAP_SIZE))
                                .setEvictionPolicy(LRU)
                                /*
                                   cache will be available until it will remove manually.
                                   less then 0 means never expired.
                                */
                                .setTimeToLiveSeconds(-1))
                // create another cache map
                .addMapConfig(
                        new MapConfig()
                                .setName("Configs")
                                .setMaxSizeConfig(new MaxSizeConfig(100,
                                        FREE_HEAP_SIZE))
                                .setEvictionPolicy(LRU)
                                // cache will expire after 2 minutes
                                .setTimeToLiveSeconds(120));
    }
}