package com.dados.domain;



import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;


import com.dados.exceptions.*;



public class Game {
	
	@Transient
    public static final String SEQUENCE_GAME = "game_sequence";
	
	@Id
	private int id;
	
	private int dice1;
	
	
	private int dice2;

	public Game() {}
	public Game(int dice1, int dice2) throws DiceOutOfRange {
		if (dice1<1 || dice1>6 || dice2<1 || dice2>6) throw new DiceOutOfRange("The dice is out of range");
		this.dice1 = dice1;
		this.dice2 = dice2;
	}
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	public int getDice1() {
		return dice1;
	}

	public void setDice1(int dice1) throws DiceOutOfRange {
		if (dice1<1 || dice1>6 ) throw new DiceOutOfRange("The dice 1 introduced is out of range");
		this.dice1 = dice1;
	}

	public int getDice2() {
		return dice2;
	}

	public void setDice2(int dice2) throws DiceOutOfRange {
		if ( dice2<1 || dice2>6) throw new DiceOutOfRange("The dice 2 introduced is out of range");
		this.dice2 = dice2;
	}


	
	public boolean hasWin() {
		if(dice1+dice2==7) {
		return true;}
		return false;}

	

}
