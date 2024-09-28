package com.kodilla.outdoor.equiprent;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Outdoor Equipment Rent API"))
public class OutdoorEquipRentApplication {
    public static void main(String[] args) {
        SpringApplication.run(OutdoorEquipRentApplication.class, args);
    }
}
