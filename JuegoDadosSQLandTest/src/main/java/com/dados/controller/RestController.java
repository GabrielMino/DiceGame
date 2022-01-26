package com.dados.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.dados.service.*;
import com.dados.domain.*;
import com.dados.exceptions.*;

@Controller
public class RestController {
	
	
	@Autowired 
	PlayerService playerService;
	
	@Autowired
	GameService gameService;
	
	//GET: /  return all players and games for each player
	
	@GetMapping("/")
	public ResponseEntity<List<Player>> getPlayers(){
	
	return new ResponseEntity<List<Player>>(playerService.getPlayers(),HttpStatus.FOUND);
	}
	
	//POST: /players : create a player
	@PostMapping("/players")
	public ResponseEntity<Player> createPlayer(@RequestBody Player player) throws PlayerAlreadyExists{
	return new ResponseEntity<Player>(playerService.savePlayer(player),HttpStatus.CREATED);
	}
	
	
	//PUT /players/{id} : modify player name
	//The name has to be introduced as a json file
	@PutMapping("/players/{id}/")
	public ResponseEntity<Player> updatePlayerName(@PathVariable(name="id") int id,@RequestBody Player playerName) throws ResourceNotFound, PlayerAlreadyExists{
		return ResponseEntity.ok(playerService.updatePlayerName(id,playerName));
	}
	
	//POST /players/{id}/games/ : add a game to a specific player
	
	@PostMapping("/players/{id}/games")
	public ResponseEntity<Game> addGame(@PathVariable(name="id") int id, @RequestBody Game game ) throws ResourceNotFound, DiceOutOfRange{
		
		return new ResponseEntity<Game>(gameService.addGame(id,game),HttpStatus.CREATED);
	}
	
	//DELETE /players/{id}/games: delete all the games of a player
	@DeleteMapping("/players/{id}/games")
	public ResponseEntity<String> deleteGamesById(@PathVariable(name="id") int id) throws ResourceNotFound{
		
		gameService.deleteGamesById(id);
		
		return new ResponseEntity<String>("Games correctly deleted!",HttpStatus.OK);	
	}
	
	//GET /players/: return a list of the players, with the correspondent average win rate
	@GetMapping("/players/")
	public ResponseEntity<Map<String,Double>> averageWinRate(){
		
		return ResponseEntity.ok(playerService.averageWinRate());	
	}
	
	//GET /players/{id}/games: return a list of the games to an specific player
	@GetMapping("/players/{id}/games")
	public ResponseEntity<List<Game>> getGamesByPlayer(@PathVariable(name="id") int id) throws ResourceNotFound{
		
		return new ResponseEntity<List<Game>>(playerService.getGames(id),HttpStatus.FOUND);	
	
	}
	
	//GET /players/ranking: returns the ranking of the players, in descending order
	@GetMapping("/players/ranking")
	public ResponseEntity<List<String>> getRanking(){
		
		return new ResponseEntity<List<String>>(playerService.getRanking(),HttpStatus.FOUND);	
	
	}
	
	//GET /players/ranking/loser: returns the player with the lowest average wins rate
	@GetMapping("/players/ranking/loser")
	public ResponseEntity<String> getRankingLoser(){
		return new ResponseEntity<String>(playerService.getLoser(),HttpStatus.FOUND);
	}
	
	//GET /players/ranking/winner: returns the player with the highest average wins rate
	
	@GetMapping("/players/ranking/winner")
	public ResponseEntity<String> getRankingWinner(){

		return new ResponseEntity<String>(playerService.getWinner(),HttpStatus.FOUND);
	}
	
	
	
	
}
