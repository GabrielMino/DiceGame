package com.dados.entity;


import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "players")
public class Player {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	
	@Column(name="name",length=30)
	private String name;
	
	@Column(name="date",length=10)
	@Temporal(TemporalType.TIMESTAMP)
    private  java.util.Date date;

	

	public Player() {
		this.name= "ANONYMOUS";
	}

	public Player(String name) {
		this.name = name;	
	}
	
	public Player(String name, Date date) {
		this.name= name;
		this.date=date;
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

	

}


