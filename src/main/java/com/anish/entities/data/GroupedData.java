package com.anish.entities.data;

import java.util.Date;

public class GroupedData {
	
	private Date receivedDate;
	private Float amount;
	private String name;
	private String currency;
	
	public GroupedData(Date receivedDate, Float amount, String currency, String name) {
		System.out.println(" constructor called with data receivedDate="+receivedDate+",amount="+amount+",currency="+currency+",name="+name);
		this.receivedDate = receivedDate;
		this.amount = amount;
		this.name = name;
		this.currency = currency;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "GroupedData [receivedDate=" + receivedDate + ", amount=" + amount + ", name=" + name + ", currency="
				+ currency + "]";
	}
	
	
	
	
}
