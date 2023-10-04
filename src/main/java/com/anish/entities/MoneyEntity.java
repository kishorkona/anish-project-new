package com.anish.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="money")
public class MoneyEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "received_date", nullable = true)
	private Date receivedDate;
	
	@Column(name = "amount", nullable = true)
	private Float amount;
	
	@Column(name = "userid", nullable = true)
	private long userid;
	
	@Column(name = "receivedFromId", nullable = true)
	private long receivedFromId;
	
	@Column(name = "currency", nullable = true)
	private String currency;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	

	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	public long getReceivedFromId() {
		return receivedFromId;
	}
	public void setReceivedFromId(long receivedFromId) {
		this.receivedFromId = receivedFromId;
	}
	
	
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "MoneyEntity [id=" + id + ", receivedDate=" + receivedDate + ", amount=" + amount + ", userid=" + userid
				+ ", receivedFromId=" + receivedFromId + ", currency=" + currency + "]";
	}
	

}
	
