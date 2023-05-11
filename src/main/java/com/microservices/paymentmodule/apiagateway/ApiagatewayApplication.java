package com.microservices.paymentmodule.apiagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiagatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiagatewayApplication.class, args);
	}

}
