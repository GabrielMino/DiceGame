package com.dados.domain;

import java.util.*;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "player")
public class Player {
	
	//variable to generate autoincrement id
	@Transient
    public static final String SEQUENCE_PLAYER = "player_sequence";
	
	@Id
	private int id ;
	
	private String name;
	
    private  Date date;
	
	private List<Game> games = new ArrayList<Game>();

	
	public Player() {
	}

	public Player(String name) {
		this.name = name;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<Game> getGames() {
		return games;
	}
	

	public void setGame(Game game) {
		games.add(game);
	}
	
	
	
	public double winsRate(){
		
		if (games.size()==0) {
			return 0;}
		
		double wins=0;
		for (Game game: games) {
			if (game.hasWin()) { wins++;}}	
		return (wins/games.size()*100);
		}


}


