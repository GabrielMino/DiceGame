package com.dados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dados.domain.Game;

@Repository
public interface GameRepository extends JpaRepository<Game,Integer> {

}
