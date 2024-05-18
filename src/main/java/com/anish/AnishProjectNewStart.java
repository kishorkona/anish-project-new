package com.anish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages = "com.anish")
@EnableWebMvc
public class AnishProjectNewStart {

	public static void main(String[] args) {
		SpringApplication.run(AnishProjectNewStart.class, args);
	}

}
