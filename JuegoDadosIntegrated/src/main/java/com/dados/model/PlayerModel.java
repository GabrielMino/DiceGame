package com.dados.model;

import java.util.List;


public class PlayerModel {
	
	private int id;
	
	private String name;
	
    private  java.util.Date date;
    
    private List<GameModel> games;
    
    public PlayerModel(){}
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public List<GameModel> getGames() {
		return games;
	}

	public void setGames(List<GameModel> games) {
		this.games = games;
	}

	public double winsRate(){
		
		if (games.size()==0) {
			return 0;}
		
		double wins=0;
		for (GameModel game: games) {
			if (game.hasWin()) { wins++;}}	
		return (wins/games.size())*100;
		}
    
    

}
