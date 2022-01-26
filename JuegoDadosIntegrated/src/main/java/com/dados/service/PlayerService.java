package com.dados.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dados.document.Game;
import com.dados.entity.Player;
import com.dados.exceptions.PlayerAlreadyExists;
import com.dados.exceptions.ResourceNotFound;
import com.dados.model.GameModel;
import com.dados.model.PlayerModel;
import com.dados.repository.GameRepository;
import com.dados.repository.PlayerRepository;

@Service
public class PlayerService {

@Autowired
PlayerRepository playerRepository;

@Autowired
GameRepository gameRepository;


public List<PlayerModel> getPlayers() {
	 List<PlayerModel> players = new ArrayList<>();
     List<Player> playerList = playerRepository.findAll();
     if (playerList.size() > 0){ //If the above list is not empty then return the list after unwrapping all the records
         playerList.stream().forEach(p -> { //Traverse through the reords
             PlayerModel playerModel = new PlayerModel();
             playerModel.setId(p.getId());
             playerModel.setName(p.getName());
             playerModel.setDate(p.getDate());
             List<GameModel> games = new ArrayList<>();
             List<Game> gameList = gameRepository.findGameByPlayerId(p.getId());
             
               //Fetch all the courses by email ID.
            
             if (gameList.size() > 0){
                 gameList.stream().forEach(g -> {
                     GameModel gameModel = new GameModel();
                     BeanUtils.copyProperties(g, gameModel);
                     games.add(gameModel);
                 });
             }
             playerModel.setGames(games);
             players.add(playerModel);
         });
     }
     return players;
 }
	


public List<GameModel> getGames(int id) throws ResourceNotFound {
	boolean player = playerRepository.existsById(id);
	if (player) {
		List<GameModel> games = new ArrayList<>();
        List<Game> gameList = gameRepository.findGameByPlayerId(id);
        if (gameList.size() > 0){
            gameList.stream().forEach(g -> {
                GameModel gameModel = new GameModel();
                BeanUtils.copyProperties(g, gameModel);
                games.add(gameModel);
       });
       return games;
    } 
	else {throw new ResourceNotFound("There is no games for this player");}
	}
	else {throw new ResourceNotFound("There is no player with this id");}
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
	if (unique)  throw new PlayerAlreadyExists("Player with name "+ name +" ,already exists!");
	if ( name!= null && name.length()>0 && !Objects.equals(player.getName(),name)){
	player.setName(name);
	playerRepository.save(player);
		}
	
	
	return player;	
	
}

public Game addGame(int id) throws ResourceNotFound {
	
	boolean player = playerRepository.existsById(id);
	if (player) {
	Game game = new Game();
	game.rollDices();
	game.setPlayerId(id);
	gameRepository.save(game);
	return game;}
	else {throw new ResourceNotFound("There is no player with this id");}
}


public void deleteGamesById(int id) throws ResourceNotFound {
	
	boolean player = playerRepository.existsById(id);
	if (player) {
		gameRepository.deleteAllByPlayerId(id);
	}
	else {throw new ResourceNotFound("There is no player with this id");}
}




}
