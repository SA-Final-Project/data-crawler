package edu.miu.changedetectorservice;

import edu.miu.changedetectorservice.service.impl.StandardDeviationServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChangeDetectorServiceApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ChangeDetectorServiceApplication.class, args);
    }

    @Autowired
    private StandardDeviationServiceIml standardDeviationServiceIml;
    @Override
    public void run(String... args) throws Exception {
        standardDeviationServiceIml.test();
    }
}