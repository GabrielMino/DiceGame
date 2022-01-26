package com.dados.repository;
import com.dados.document.Game;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game,Integer> {
	
	void deleteAllByPlayerId(int playerId);
	List<Game> findGameByPlayerId(int playerId);
} 






