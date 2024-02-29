package com.rewcode.blog;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "SpringBoot Blog Rest APIs",
				description = "SpringBoot Blog App Rest APIs Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Orlando",
						email = "orlandocruz999@gmail.com",
						url = "https://github.com/Orlando9991/blog-rest-api"),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.com/Orlando9991/blog-rest-api"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Springboot Blog App Documentation",
				url = "https://github.com/Orlando9991/blog-rest-api"
		)


)
public class BlogRestApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		 return new ModelMapper();
	 }
	public static void main(String[] args) {
		SpringApplication.run(BlogRestApiApplication.class, args);
	}
}
