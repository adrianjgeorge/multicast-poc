package org.openrepose.poc.multicast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableScheduling
public class OpenshiftMulticastApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenshiftMulticastApplication.class, args);
    }

    @Bean
    public Map<String,Node> getHostList() {
        return new HashMap<>();
    }
}
