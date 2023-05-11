package com.intership.app.habittracker;

import com.intership.app.habittracker.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class HabittrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabittrackerApplication.class, args);
	}

}
