package com.dados.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dados.domain.Game;
import com.dados.domain.Player;
import com.dados.repository.*;
import com.dados.exceptions.*;



@Service
public class PlayerService {
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	PlayerRepository playerRepository;


	public List<Player> getPlayers() {
		 return playerRepository.findAll();
	}

	public Player savePlayer(Player player) throws PlayerAlreadyExists {
		
		Date now = new Date(); // This object contains the current date value
		if (player.getName()=="" || player.getName()=="ANONYMOUS" || player.getName()==null) {
			player.setDate(now);
			player.setName("ANONYMOUS");
			playerRepository.save(player);
		} else {
		boolean unique = playerRepository.existsByName(player.getName());
		if (unique)  throw new PlayerAlreadyExists("Player with name "+ player.getName() +" ,already exists!");
		
		player.setDate(now);
		playerRepository.save(player);
		}
		return player;
	}

	public Player updatePlayerName(int id,Player playerName ) throws ResourceNotFound, PlayerAlreadyExists {
		
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		String name = playerName.getName();
		boolean unique = playerRepository.existsByName(name);
		// the player can only be updated with a valid name, it cannot be anonymous
		if ( name!= null && name.length()>0) {
			if (unique)  throw new PlayerAlreadyExists("Player with name "+ name +" ,already exists!");{
			player.setName(name);
			playerRepository.save(player);
			return player;}}
		else {throw new PlayerAlreadyExists("The name entered is not valid");}
	}

	// it will be used a hashmap, because it´s easy to convert into a json object
	public Map<String, Double> averageWinRate() {
		List<Player> players=playerRepository.findAll();
		HashMap<String,Double> result = new HashMap <String,Double>();
		//this value is added in order to differentiate between anonymuos players
		int valAnonymous = 1;
		for (Player player : players) {
			if (player.getName() == "ANONYMOUS"){
				result.put("ANONYMOUS"+valAnonymous++, DoubleRounder.round(player.winsRate(),2));
			}else {
			//DoubleRounder has to be added in pom
				result.put(player.getName(), DoubleRounder.round(player.winsRate(),2));}
			}
			return result;}
	

	public List<Game> getGames(int id) throws ResourceNotFound {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		if(player.getGames().isEmpty()) throw new ResourceNotFound("There is no game for this player");
		else return player.getGames();
		
	}

	public List<String> getRanking() {
		List<Player> players = playerRepository.findAll();
		List<String> result = new ArrayList<String>();
		players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
		for(Player player:players ) {
			result.add("Name: "+player.getName()+" ,WinsRate: "+ DoubleRounder.round(player.winsRate(),2));
		}
		return result;	
	}


	public String getLoser() {
		List<Player> players = playerRepository.findAll();
		players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
		
		return players.get(players.size()-1).getName();
	}
	
	public String getWinner() {
		List<Player> players = playerRepository.findAll();
		players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
		
		return players.get(0).getName();
	}
	
	

}
