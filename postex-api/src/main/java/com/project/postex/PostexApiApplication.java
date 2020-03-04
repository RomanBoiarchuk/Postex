package com.project.postex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class PostexApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostexApiApplication.class, args);
	}

}
