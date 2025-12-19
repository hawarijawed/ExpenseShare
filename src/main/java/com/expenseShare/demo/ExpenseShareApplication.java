package com.expenseShare.demo;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.sql.DataSourceDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;


@SpringBootApplication

public class ExpenseShareApplication {

	@Value("${spring.name}")
	private static String name;


	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
//		System.setProperty("name", dotenv.get("name"));
//		System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kathmandu"));
		System.out.println("Environment variable: "+name);

		SpringApplication.run(ExpenseShareApplication.class, args);
	}

	@PostConstruct
	public void run(){
		System.out.println("Running Post Construct Method!!!!!!!!!!!!!");

	}

}
