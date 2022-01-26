
package com.dados.controller;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dados.service.*;
import com.dados.domain.*;
import com.dados.exceptions.*;

@RestController
@RequestMapping("/")
public class Controller {
	
	
	@Autowired 
	PlayerService playerService;


	@GetMapping("/upload")
	public ResponseEntity<String> uploadDataFromJsonFiles() {
	
	playerService.uploadDataFromJsonFiles();
	
	return new ResponseEntity<String>("Database correctly uploaded!",HttpStatus.CREATED);
	}
	
	//GET: /  devuelve todos los players
	@GetMapping("/")
	public ResponseEntity<List<Player>> getPlayers() throws ResourceNotFound{
	
	return new ResponseEntity<List<Player>>(playerService.getPlayers(),HttpStatus.FOUND);
	
	}
	
	
	//POST: /players : crea un jugador
	
	@PostMapping("/players")
	public ResponseEntity<Player> createPlayer(@RequestBody Player player) throws PlayerAlreadyExists{
	return new ResponseEntity<Player>(playerService.saveNewPlayer(player),HttpStatus.FOUND);
	}
	
	//PUT /players/{id} : modifica el nombre del jugador
	@PutMapping("/players/{id}/")
	public ResponseEntity<Player> modifyPlayerName(@PathVariable(name="id") int id,@RequestBody Player playerName) throws ResourceNotFound, PlayerAlreadyExists{
		return new ResponseEntity<Player>(playerService.modifyName(id,playerName),HttpStatus.CREATED);
	}
	
	//POST /players/{id}/games/ : un jugador específico realiza un tirón de los dados.
	
	@PostMapping("/players/{id}/games")
	public ResponseEntity<Game> addGame(@PathVariable(name="id") int id, @RequestBody Game game ) throws ResourceNotFound, DiceOutOfRange{
		Game gameAdded =playerService.addGame(id,game);
		
		return new ResponseEntity<Game>(gameAdded,HttpStatus.CREATED);
	}
	
	//DELETE /players/{id}/games: elimina las tiradas del jugador
	
	@DeleteMapping("/players/{id}/games")
	public ResponseEntity<String> deleteGames(@PathVariable(name="id") int id) throws ResourceNotFound{
		
		playerService.deleteAll(id);
		
		return new ResponseEntity<String>("Games correctly deleted!",HttpStatus.ACCEPTED);	
	}
	
	//GET /players/: devuelve el listado de todos los jugadores del sistema con su porcentaje medio de éxitos
	@GetMapping("/players/")
	public ResponseEntity<Map<String,Double>> averageWinRate() throws ResourceNotFound{
		
		return new ResponseEntity<Map<String, Double>>(playerService.averageWinRate(),HttpStatus.FOUND);	
	}
	

	//GET /players/{id}/games: devuelve el listado de jugadas por un jugador.
	@GetMapping("/players/{id}/games")
	public ResponseEntity<List<Game>> getGamesByPlayer(@PathVariable(name="id") int id) throws ResourceNotFound{
		
		return new ResponseEntity<List<Game>>(playerService.getGames(id),HttpStatus.FOUND);	
	
	}
	
	//GET /players/ranking: devuelve el ranking medio de todos los jugadores del sistema . Es decir, el porcentaje medio de logros.
	@GetMapping("/players/ranking")
	public ResponseEntity<List<String>> getRanking() throws ResourceNotFound{
		
		return new ResponseEntity<List<String>>(playerService.getRanking(),HttpStatus.FOUND);	
	
	}
	
	//GET /players/ranking/loser: devuelve al jugador con peor porcentaje de éxito
	@GetMapping("/players/ranking/loser")
	public ResponseEntity<String> getRankingLoser() throws ResourceNotFound{
		
		return new ResponseEntity<String>(playerService.getLoser(),HttpStatus.FOUND);
	}
	
	//GET /players/ranking/winner: devuelve al jugador con peor porcentaje de éxito
	
	@GetMapping("/players/ranking/winner")
	public ResponseEntity<String> getRankingWinner() throws ResourceNotFound{
		return new ResponseEntity<String>(playerService.getWinner(),HttpStatus.FOUND);
	}
	
	
	
	
}
