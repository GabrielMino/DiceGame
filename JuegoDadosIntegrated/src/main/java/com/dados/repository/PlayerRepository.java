package com.dados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dados.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer> {
	
	boolean existsByName(String name);
	

}
