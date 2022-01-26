package com.dados;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dados.domain.Player;
import com.dados.repository.PlayerRepository;
import com.dados.service.PlayerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class JuegoDadosMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JuegoDadosMongoApplication.class, args);
	}
	/*
	@Bean
	CommandLineRunner runner(PlayerRepository repository) {
		return args ->{
			Player player = new Player();
					
		};
	}
	
	
	@Bean
	CommandLineRunner runner(PlayerService playerService){
	    return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Player>> typeReference = new TypeReference<List<Player>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
			try {
				List<Player> users = mapper.readValue(inputStream,typeReference);
				playerService.save(users);
				System.out.println("Users Saved!");
			} catch (IOException e){
				System.out.println("Unable to save users: " + e.getMessage());
			}
	    };
}

*/
}
