package com.dados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.decimal4j.util.DoubleRounder;
import org.junit.jupiter.api.MethodOrderer;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.dados.domain.*;
import com.dados.exceptions.DiceOutOfRange;
import com.dados.exceptions.PlayerAlreadyExists;
import com.dados.exceptions.ResourceNotFound;
import com.dados.repository.*;
import com.dados.service.*;





@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes=SQLServiceTest.class)
public class SQLServiceTest {
	
	@Mock
	PlayerRepository playerRepository;
	
	@Mock
	GameRepository gameRepository;
	
	@InjectMocks
	PlayerService playerService;
	
	@InjectMocks
	GameService gameService;
	
	//public List<Player> players;
	
	@Test
	@Order(1)
	public void test_getPlayers() {
		
		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player1"));
		players.add(new Player("Player2"));
		players.add(new Player());
		
		when(playerRepository.findAll()).thenReturn(players);//Mocking
		
		assertEquals(3,playerService.getPlayers().size());
	}
	
	@Test
	@Order(2)
	public void test_savePlayer() throws PlayerAlreadyExists{
		
		Player player = new Player("Player1");
		when(playerRepository.existsByName(player.getName())).thenReturn(false);//Mocking
		when(playerRepository.save(player)).thenReturn(player);//Mocking
		assertEquals(player,playerService.savePlayer(player));
	}
	
	@Test
	@Order(3)
	public void test_updatePlayerName() throws ResourceNotFound, PlayerAlreadyExists {
		int id = 1;
		Player player = new Player("Hola");
		player.setId(id);
		when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
		when(playerRepository.existsByName(player.getName())).thenReturn(false);//Mocking
		assertEquals(player,playerService.updatePlayerName(id, player));
	}
	
	
	@Test 
	@Order(4)
	public void test_averageWinRate() throws DiceOutOfRange{
		
		Player player = new Player("Player1");
		Game game1 = new Game(5,4);
		Game game2 = new Game(3,4);
		player.setGame(game1);
		player.setGame(game2);
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		
		//Mocking
		when(playerRepository.findAll()).thenReturn(players);
		
		// Since we just have one player, we can get the key and value for the first(and unique) element.
		Map<String, Double> result = playerService.averageWinRate();
		Map.Entry<String,Double> entry = result.entrySet().iterator().next();
		String name = entry.getKey();
		Double winsRate =entry.getValue();
		//test
		assertEquals(player.getName(),name);
		assertEquals(DoubleRounder.round(player.winsRate(),2),winsRate);
	
	}
	
	@Test
	@Order(5)
	public void test_getGames() throws DiceOutOfRange, ResourceNotFound {
			
			Player player = new Player("Player1");
			int id = 1;
			player.setId(1);
			Game game = new Game(5,1);
			game.setPlayer(player);
			player.setGame(game);
			
			when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
			
			assertEquals(player.getGames(),playerService.getGames(id));
		}
		
	
	//On this test i will include getRanking(), getLoser(), getWinner() functions of the service.
	@Test
	@Order(6)
	public void test_getRanking_getLoser_getWinner() throws DiceOutOfRange {
		//create 3 players with their respectives games
		Player player1 = new Player("Player1");
		Game game1 = new Game(5,2);
		Game game2 = new Game(3,4);
		player1.setGame(game1);
		player1.setGame(game2);
		
		Player player2 = new Player("Player2");
		Game game3 = new Game(5,4);
		Game game4 = new Game(6,1);
		player1.setGame(game3);
		player1.setGame(game4);
		
		Player player3 = new Player();
		List<Player> players = new ArrayList<Player>();
		
		//add according to the winRate
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		//get the order list
		List<String> result = new ArrayList<String>();
		for(Player player:players ) {
			result.add("Name: "+player.getName()+" ,WinsRate: "+ DoubleRounder.round(player.winsRate(),2));
		}
		
		//Mocking
		when(playerRepository.findAll()).thenReturn(players);
		//service
		List<String> serviceResult = playerService.getRanking();
		//test getRanking()
		assertEquals(result,serviceResult);
		//test getLoser()
		assertEquals(player3.getName(),playerService.getLoser());
		//test getWinner()
		assertEquals(player1.getName(),playerService.getWinner());

	}
	
	@Test
	@Order(7)
	public void test_addGame () throws ResourceNotFound, DiceOutOfRange {
		
		Player player = new Player("Player1");
		int id = 1;
		player.setId(1);
		Game game = new Game(5,6);
		game.setPlayer(player);
		
		when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
		when(gameRepository.save(game)).thenReturn(game);//Mocking
		
		assertEquals(game.getDice1(),gameService.addGame(id,game).getDice1());
		assertEquals(game.getDice2(),gameService.addGame(id,game).getDice2());
		}
		
	
	
	@Test
	@Order(8)
	public void test_deleteGamesById () throws DiceOutOfRange, ResourceNotFound {
		
		Player player = new Player("Player1");
		int id = 1;
		player.setId(id);
		Game game1 = new Game(5,2);
		Game game2 = new Game(3,4);
		player.setGame(game1);
		player.setGame(game2);
		game1.setPlayer(player);
		game2.setPlayer(player);
		
		when(playerRepository.findById(id)).thenReturn(Optional.of(player));//Mocking
		gameService.deleteGamesById(1);
		verify(gameRepository,times(1)).deleteAll(player.getGames());
		
	}
	
	
}
