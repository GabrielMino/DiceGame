package com.dados.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dados.domain.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player,Integer> {
	
	boolean existsByName(String name);

}
