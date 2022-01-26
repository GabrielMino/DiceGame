package com.dados.service;


import java.io.File;
import java.io.FileReader;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dados.exceptions.*;


@Service
public class PlayerService {
	
	
	
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	SequenceGeneratorPlayerService sequenceGeneratorPlayer;
	
	@Autowired
	SequenceGeneratorGameService sequenceGeneratorGame;
	


	public List<Player> getPlayers() throws ResourceNotFound {
		List<Player> players = playerRepository.findAll();
		if(players.size()>0) { return players;}
		else {throw new ResourceNotFound("There isn´t any player yet!");}}


	public void uploadDataFromJsonFiles() {
		// create object mapper instance
				ObjectMapper mapper = new ObjectMapper();
				// define the folder
				File dir = new File("src/main/resources/data/");
				// get the path list
				File[] directoryListing = dir.listFiles();
				if (directoryListing != null) {
				   for (File child: directoryListing) {
					   try {
					   FileReader file = new FileReader(child);
					   // convert JSON string to Player object
					   Player player = mapper.readValue(file, Player.class);
					   //set autoincrement ID
					   player.setId(sequenceGeneratorPlayer.generateSequence(player.SEQUENCE_PLAYER));
					   for (Game game: player.getGames()) {
						   game.setId(sequenceGeneratorGame.generateSequence(game.SEQUENCE_GAME));
					   }
					    playerRepository.save(player);}
					    catch (Exception ex) {
						    ex.printStackTrace();
						}
					   }
				   }
		

		
	}

	public Player saveNewPlayer(Player player) throws PlayerAlreadyExists {
		
		Date now = new Date(); // This object contains the current date value
		if (player.getName()=="" || player.getName()=="ANONYMOUS" || player.getName()==null) {
			player.setDate(now);
			player.setId(sequenceGeneratorPlayer.generateSequence(player.SEQUENCE_PLAYER));
			player.setName("ANONYMOUS");
			playerRepository.save(player);
		} else {
		boolean unique = playerRepository.existsByName(player.getName());
		if (unique)  throw new PlayerAlreadyExists("Player with name "+ player.getName() +" ,already exists!");
		// set actual date
		player.setDate(now);
		//set autoincrement id
		player.setId(sequenceGeneratorPlayer.generateSequence(player.SEQUENCE_PLAYER));
		playerRepository.save(player);
		}
		return player;	
	}

public Player modifyName(int id,Player playerName ) throws ResourceNotFound, PlayerAlreadyExists {
		
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		String name = playerName.getName();
		boolean unique = playerRepository.existsByName(name);
		if (unique)  throw new PlayerAlreadyExists("Player with name "+ name +" ,already exists!");
		if ( name!= null && name.length()>0 && !Objects.equals(player.getName(),name)){
			player.setName(name);
			playerRepository.save(player);
			}
		return player;	
	}


	public Map<String, Double> averageWinRate() throws ResourceNotFound {
		List<Player> players=playerRepository.findAll();
		if(players.size()>0) {
		HashMap<String,Double> result = new HashMap <String,Double>();
		//this value is added in order to differentiate between anonymuos players
		int valAnonymous = 1;
		for (Player player : players) {
			 if (player.getName() == "ANONYMOUS"){
					result.put("ANONYMOUS"+valAnonymous++, DoubleRounder.round(player.winsRate(),2));
				}else {
				//DoubleRounder has to be added in pom
					result.put(player.getName(), DoubleRounder.round(player.winsRate(),2));}}
			return result;
	}else {throw new ResourceNotFound("There isn´t any player yet!");}}
	
	
	public Game addGame(int id, Game game) throws ResourceNotFound, DiceOutOfRange {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		
		//if the client tries to insert a new game with an incorrect json body(without the categories dice1 and dice2)
		// a new Game will be generated,with dice1:0 and dice:0 values by default, and without throwing an error!
		Game newGame = new Game(game.getDice1(),game.getDice2());
		newGame.setId(sequenceGeneratorGame.generateSequence(game.SEQUENCE_GAME));
		player.setGame(newGame);
		playerRepository.save(player);
		
		return newGame;
		
	}
	

	public List<Game> getGames(int id) throws ResourceNotFound {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		if(player.getGames().isEmpty()) throw new ResourceNotFound("There is no game for this player");
		else return player.getGames();
		
	}


	public List<String> getRanking() throws ResourceNotFound {
		List<Player> players = playerRepository.findAll();
		if(players.size()>0) {
			List<String> result = new ArrayList<String>();
			players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
			for(Player player:players ) {
				result.add("Name: "+player.getName()+" ,WinsRate: "+ DoubleRounder.round(player.winsRate(),2));}
			return result;	}
		else {throw new ResourceNotFound("There isn´t any player yet!");}}
	


	public String getLoser() throws ResourceNotFound {
		List<Player> players = playerRepository.findAll();
		if(players.size()>0) {
			players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
			return players.get(players.size()-1).getName();}
		else {throw new ResourceNotFound("There isn´t any player yet!");}}

	public String getWinner() throws ResourceNotFound {
		List<Player> players = playerRepository.findAll();
		if(players.size()>0) {
			players.sort(Comparator.comparingDouble(Player::winsRate).reversed());
			return players.get(0).getName();}
		{throw new ResourceNotFound("There isn´t any player yet!");}}
	


	public void deleteAll(int id) throws ResourceNotFound {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
			player.getGames().clear();
			playerRepository.save(player);
		}

		
	}
	
	
	



