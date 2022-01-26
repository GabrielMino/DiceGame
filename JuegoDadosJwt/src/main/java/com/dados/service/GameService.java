package com.dados.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dados.domain.Game;
import com.dados.domain.Player;
import com.dados.exceptions.DiceOutOfRange;
import com.dados.exceptions.ResourceNotFound;
import com.dados.repository.GameRepository;
import com.dados.repository.PlayerRepository;

@Service
public class GameService {
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	PlayerRepository playerRepository;

	public Game addGame(int id, Game game) throws ResourceNotFound, DiceOutOfRange {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		////if the client tries to insert a new game with an incorrect json body(without the categories dice1 and dice2)
		// a new Game will be generated,with dice1:0 and dice:0 values by default, and without throwing an error!
		Game newGame = new Game(game.getDice1(),game.getDice2());
		newGame.setPlayer(player);
		player.setGame(newGame);
		gameRepository.save(newGame);
		
		return newGame;
		
	}

	public void deleteGamesById(int id) throws ResourceNotFound {
		Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFound("There is no player with this id"));
		gameRepository.deleteAll(player.getGames());
		player.getGames().clear();
	}
	
	

}
