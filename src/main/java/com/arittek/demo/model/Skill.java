package com.arittek.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class Skill {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
    private String label;
    private String description;

    public Skill() {
		super();
    }

    public Skill(String label, String description) {
		super();
		this.label = label;
		this.description = description;
	}

}
