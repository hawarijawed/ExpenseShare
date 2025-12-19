package com.expenseShare.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseShareApplication {

	@Value("${spring.name}")
	private static String name;
	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("name", dotenv.get("name"));

		System.out.println("Environment variable: "+name);
		SpringApplication.run(ExpenseShareApplication.class, args);
	}

}
