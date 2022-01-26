package com.dados.document;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.dados.exceptions.*;


@Document(collection = "game")
public class Game {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private int dice1;
	
	private int dice2;
	
	private  int playerId; 
	
	public Game() {
		
	}
	public Game(int dice1, int dice2) {
		
		this.dice1 = dice1;
		this.dice2 = dice2;
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public int getDice1() {
		return dice1;
	}

	public void setDice1(int dice1) {
		this.dice1 = dice1;
	}

	public int getDice2() {
		return dice2;
	}

	public void setDice2(int dice2) {
		this.dice2 = dice2;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public boolean hasWin() {
		if(dice1+dice2==7) {
		return true;}
		return false;}
	
	public void rollDices() {
		 this.dice1=(int) Math.floor(Math.random()*6+1);
		 this.dice2=(int) Math.floor(Math.random()*6+1);
		}

}
