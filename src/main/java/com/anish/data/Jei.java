package com.anish.data;

import java.time.LocalDate;

public class Jei {
	
	private String name;
	private String subject;
	private String bookId;
	private String topic;
	private LocalDate date;
	
	
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	@Override
	public String toString() {
		return "Jei [name=" + name + ", subject=" + subject + ", bookId=" + bookId + ", topic=" + topic + ", date="
				+ date + "]";
	}
	
	

}