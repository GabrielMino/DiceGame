package com.dados.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dados.entity.*;
import com.dados.document.*;
import com.dados.service.*;
import com.dados.model.*;
import com.dados.exceptions.PlayerAlreadyExists;
import com.dados.exceptions.ResourceNotFound;

@RestController
public class Controller {
	
	
	//GET: /  devuelve todos los players
	
	@GetMapping("/")
	public ResponseEntity<List<PlayerModel>> getPlayers(){
		
	return new ResponseEntity<List<PlayerModel>>(playerService.getPlayers(),HttpStatus.FOUND);
	}
		
	//GET /players/{id}/games: devuelve el listado de jugadas por un jugador.
	@GetMapping("/players/{id}/games")
	public ResponseEntity<List<GameModel>> getGamesByPlayer(@PathVariable(name="id") int id) throws ResourceNotFound{
		return new ResponseEntity<List<GameModel>>(playerService.getGames(id),HttpStatus.FOUND);	
	}

		
	
	//POST: /players : crea un jugador
	
	@Autowired
	PlayerService playerService;
	
	@PostMapping("/players")
	public ResponseEntity<Player> createPlayer(@RequestBody Player player) throws PlayerAlreadyExists{
		return new ResponseEntity<Player>(playerService.savePlayer(player),HttpStatus.CREATED);
	}
	
	
	//PUT /players/{id} : modifica el nombre del jugador

	@PutMapping("/players/{id}/")
	public ResponseEntity<Player> updatePlayerName(@PathVariable(name="id") int id,@RequestBody Player playerName) throws ResourceNotFound, PlayerAlreadyExists{
		return ResponseEntity.ok(playerService.updatePlayerName(id,playerName));
	}
	
	
	@PostMapping("/players/{id}/games")
	public ResponseEntity<Game> addGame(@PathVariable(name="id") int id ) throws ResourceNotFound{
			
		return new ResponseEntity<Game>(playerService.addGame(id),HttpStatus.OK);
		}
		
	//DELETE /players/{id}/games: elimina las tiradas del jugador
		
	@DeleteMapping("/players/{id}/games")
	public ResponseEntity<String> deleteGamesById(@PathVariable(name="id") int id) throws ResourceNotFound{
			
		playerService.deleteGamesById(id);
			
		return new ResponseEntity<String>("Games correctly deleted!",HttpStatus.OK);	
		
		}
	
	//GET /players/: devuelve el listado de todos los jugadores del sistema con su porcentaje medio de éxitos
	@GetMapping("/players/")
	public ResponseEntity<Map<String,Double>> averageWinRate() throws ResourceNotFound{
		
		List<PlayerModel> players = playerService.getPlayers();
		if(players.size()>0) {
			HashMap<String,Double> result = new HashMap <String,Double>();
		//this value is added in order to differentiate between anonymuos players
			int valAnonymous = 1;
			for(PlayerModel player:players ) {
				if (player.getName() == "ANONYMOUS"){
					result.put("ANONYMOUS"+valAnonymous++, DoubleRounder.round(player.winsRate(),2));
				}else {
					result.put(player.getName(), DoubleRounder.round(player.winsRate(),2));}
		}
			return new ResponseEntity<Map<String, Double>>(result,HttpStatus.FOUND);}
		else {throw new ResourceNotFound("There isn´t any player yet");}
		
	}
	
	//GET /players/ranking: devuelve el ranking medio de todos los jugadores del sistema . Es decir, el porcentaje medio de logros.
	@GetMapping("/players/ranking")
	public ResponseEntity<List<String>> getRanking() throws ResourceNotFound{
		
		List<PlayerModel> players = playerService.getPlayers();
		if(players.size()>0) {
		List<String> result = new ArrayList<String>();
		players.sort(Comparator.comparingDouble(PlayerModel::winsRate).reversed());
		for(PlayerModel player:players ) {
			result.add("Name: "+player.getName()+" ,WinsRate: "+ DoubleRounder.round(player.winsRate(),2));
		}
		return new ResponseEntity<List<String>>(result,HttpStatus.FOUND);}
		else {throw new ResourceNotFound("There isn´t any player yet");}
	}
	
	//GET /players/ranking/loser: devuelve al jugador con peor porcentaje de éxito
	@GetMapping("/players/ranking/loser")
	public ResponseEntity<String> getRankingLoser() throws ResourceNotFound{
		
		List<PlayerModel> players = playerService.getPlayers();
		if(players.size()>0) {
		players.sort(Comparator.comparingDouble(PlayerModel::winsRate).reversed());
		return new ResponseEntity<String>(players.get(players.size()-1).getName(),HttpStatus.FOUND);}
		else {throw new ResourceNotFound("There isn´t any player yet");}

		}
			
	//GET /players/ranking/winner: devuelve al jugador con peor porcentaje de éxito		
	@GetMapping("/players/ranking/winner")
	public ResponseEntity<String> getRankingWinner() throws ResourceNotFound{
		List<PlayerModel> players = playerService.getPlayers();
		if(players.size()>0) {
		players.sort(Comparator.comparingDouble(PlayerModel::winsRate).reversed());
		return new ResponseEntity<String>(players.get(0).getName(),HttpStatus.FOUND);}
		else {throw new ResourceNotFound("There isn´t any player yet");}
		
		}
		
	}

	
	
	
	
