package com.lab.libraryapi20;

import com.lab.libraryapi20.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Application {


	@Autowired
	private EmailService emailService;

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


	/*@Bean
	public CommandLineRunner runner(){
		return args -> {
			List<String> emails = java.util.Arrays.asList("c4f9bacab3-825f64@inbox.mailtrap.io");
			emailService.sendMails("Testando serviço de emails.", emails);
			System.out.println("envia email");
		};
	}*/

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
