package com.dados;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.dados.domain.Game;
import com.dados.domain.Player;
import com.dados.exceptions.DiceOutOfRange;
import com.dados.exceptions.PlayerAlreadyExists;
import com.dados.exceptions.ResourceNotFound;
import com.dados.repository.GameRepository;
import com.dados.repository.PlayerRepository;
import com.dados.service.GameService;
import com.dados.service.PlayerService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes=SQLExceptionsTest.class)
public class SQLExceptionsTest {
	
	@Mock
	PlayerRepository playerRepository;
	
	@Mock
	GameRepository gameRepository;
	
	@InjectMocks
	PlayerService playerService;
	
	@InjectMocks
	GameService gameService;
	
	
	//TEST PlayerAlreadyExists
	@Test
	@Order(1)
	public void test_updatePlayerName() throws ResourceNotFound, PlayerAlreadyExists {
		int id = 1;
		Player player = new Player("Player1");
		player.setId(id);
		when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
		when(playerRepository.existsByName(player.getName())).thenReturn(true);//Mocking
		
		assertThrows(PlayerAlreadyExists.class,()-> playerService.updatePlayerName(1,player));
	}
	
	//TEST ResourceNotFound
	@Test
	@Order(2)
	public void test_updatePlayerName2() throws ResourceNotFound, PlayerAlreadyExists {
		int id = 1;
		Player player = new Player("Player1");
		player.setId(id);
		//Mocking, the player is null
		when(playerRepository.findById(id)).thenReturn((Optional.ofNullable(null)));
		//the following line can be commented because error has been thrown in the previous line
		when(playerRepository.existsByName(player.getName())).thenReturn(false);
		
		assertThrows(ResourceNotFound.class,()-> playerService.updatePlayerName(1,player));
	}
	
	//PlayerAlreadyExists 2
	@Test
	@Order(3)
	public void test_updatePlayerName3() throws ResourceNotFound, PlayerAlreadyExists {
		int id = 1;
		Player player = new Player("");
		player.setId(id);
		when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
		when(playerRepository.existsByName(player.getName())).thenReturn(true);//Mocking
		
		assertThrows(PlayerAlreadyExists.class,()-> playerService.updatePlayerName(1,player));
	}
	
	//Test DiceOutOfRange
	
	@Test
	@Order(4)
	public void test_game() throws DiceOutOfRange {

	assertThrows(DiceOutOfRange.class, ()-> new Game(7,1));
	
	}
	}

