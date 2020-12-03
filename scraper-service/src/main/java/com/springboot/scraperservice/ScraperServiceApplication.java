package com.springboot.scraperservice;

import com.springboot.scraperservice.webscraper.ScraperEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ScraperServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ScraperServiceApplication.class, args);
		context.getBean(ScraperEngine.class).begin();
	}

}
