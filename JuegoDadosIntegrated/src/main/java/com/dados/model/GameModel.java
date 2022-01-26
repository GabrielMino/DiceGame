package com.dados.model;


public class GameModel {
	private String id;
	

	private int dice1;
	private int dice2;
	private  int playerId; 
	
public GameModel() {		
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
		if((int)(dice1+dice2)==7) {
		return true;}
		return false;}
		
}

