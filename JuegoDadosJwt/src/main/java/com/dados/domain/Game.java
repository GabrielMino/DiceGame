package com.dados.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dados.exceptions.DiceOutOfRange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "games")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","player"})
public class Game {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "dice1",nullable=false)
	private int dice1;
	
	@Column(name = "dice2",nullable=false)
	private int dice2;
	
	@ManyToOne()
    @JoinColumn(name = "player_id")
	private Player player;
	
	
	
	
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public boolean hasWin() {
		if(dice1+dice2==7) {
		return true;}
		return false;}
	

}
