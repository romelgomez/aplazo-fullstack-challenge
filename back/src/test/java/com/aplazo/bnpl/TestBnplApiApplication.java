package com.aplazo.bnpl;

import org.springframework.boot.SpringApplication;

public class TestBnplApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(BnplApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
