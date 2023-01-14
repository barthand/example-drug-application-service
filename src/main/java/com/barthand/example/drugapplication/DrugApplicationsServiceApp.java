package com.barthand.example.drugapplication;

import com.barthand.example.drugapplication.config.OpenFDAProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(OpenFDAProperties.class)
public class DrugApplicationsServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(DrugApplicationsServiceApp.class, args);
	}

}
