package com.anish.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="receivedfrom")
public class RecievedFromEntity {
	
	private String name;
	@Id
	private long id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "RecievedFromEntity [name=" + name + ", id=" + id + "]";
	}

}


	