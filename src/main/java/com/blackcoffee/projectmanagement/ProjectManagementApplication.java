package com.blackcoffee.projectmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.blackcoffee.projectmanagement","org.springdoc"})

@OpenAPIDefinition(
		info = @Info(
				title = "Project Management REST API Documentation",
				description = "Spring boot project REST API",
				version = "v1.0",
				contact = @Contact(
						name = "TRAN NGOC TIEN - PATRICK",
						email = "patricktrandev@gmail.com",
						url = "https://www.linkedin.com/in/patricktrandev//"
				)
		)
)
public class ProjectManagementApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApplication.class, args);
	}

}
