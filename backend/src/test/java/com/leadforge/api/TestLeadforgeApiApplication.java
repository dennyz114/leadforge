package com.leadforge.api;

import org.springframework.boot.SpringApplication;

public class TestLeadforgeApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(LeadforgeApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
