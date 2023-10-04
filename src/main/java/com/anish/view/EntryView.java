package com.anish.view;



import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class EntryView 
{
	private int userId;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date entrydate;
	private String description;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "EntryView [userId=" + userId + ", entrydate=" + entrydate + ", description=" + description + "]";
	}
	
	
	
}
