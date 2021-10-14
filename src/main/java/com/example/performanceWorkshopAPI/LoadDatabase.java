package com.example.performanceWorkshopAPI;

import com.example.performanceWorkshopAPI.xrfToken.XRFToken;
import com.example.performanceWorkshopAPI.xrfToken.XRFTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            ClassPathResource resource = new ClassPathResource("MOCK_DATA.csv");
            InputStream inputStream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while(reader.ready()) {
                String[] line = reader.readLine().split(",");
                log.info("Preloading " + repository.save(new Employee(line[0], line[1], line[2])));

            }

            log.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
        };
    }

    @Bean
    CommandLineRunner initTokenDatabase(XRFTokenRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new XRFToken("1234")));

        };
    }

}