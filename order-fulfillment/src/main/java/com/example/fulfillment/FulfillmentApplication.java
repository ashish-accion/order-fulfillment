package com.example.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
public class FulfillmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(FulfillmentApplication.class, args);
    }

    @Configuration
    public static class AsyncConfig {
        @Bean(name = "taskExecutor")
        public Executor taskExecutor() {
            return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
    }
}