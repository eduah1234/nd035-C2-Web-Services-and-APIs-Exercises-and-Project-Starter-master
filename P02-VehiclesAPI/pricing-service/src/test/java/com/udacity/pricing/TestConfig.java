package com.udacity.pricing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@EnableDiscoveryClient(autoRegister = false)
public class TestConfig {

    @Bean
    @Primary
    public DiscoveryClient discoveryClient() {
        return new DiscoveryClient() {
            @Override
            public List<ServiceInstance> getInstances(String serviceId) {
                return new ArrayList<>();
            }

            @Override
            public List<String> getServices() {
                return new ArrayList<>();
            }

            @Override
            public String description() {
                throw new UnsupportedOperationException("Unimplemented method 'description'");
            }
        };
    }
}