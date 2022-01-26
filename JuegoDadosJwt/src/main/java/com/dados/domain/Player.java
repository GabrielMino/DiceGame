package com.dados.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "players")
public class Player {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	
	@Column(name="name",unique = true,length=30)
	private String name;
	
	@Column(name="date",length=10)
	@Temporal(TemporalType.TIMESTAMP)
    private  java.util.Date date;

	@OneToMany(mappedBy = "player")
   
    private List<Game> games = new ArrayList<Game>();
	
	
	public Player() {
		this.name= "ANONYMOUS";
	}

	public Player(String name) {
		this.name = name;
		
	}

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
		this.games.add(game);
	}
	
	
	
	public double winsRate(){
		
		if (games.size()==0) {
			return 0;}
		
		double wins=0;
		for (Game game: games) {
			if (game.hasWin()) { wins++;}}	
		return (wins/games.size());
	}
	
	

}


