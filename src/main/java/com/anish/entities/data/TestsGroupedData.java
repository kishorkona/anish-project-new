package com.anish.entities.data;

import java.util.Date;

public class TestsGroupedData {

	private Date tradeDate;
	private long noOfRecords;
	private int grade;
	
	public TestsGroupedData(Date tradeDate, int grade, long noOfRecords) {
		this.tradeDate = tradeDate;
		this.noOfRecords = noOfRecords;
		this.grade = grade;
	}
	
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public long getNoOfRecords() {
		return noOfRecords;
	}
	public void setNoOfRecords(long noOfRecords) {
		this.noOfRecords = noOfRecords;
	}
	
	
	
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "TestsGroupedData [tradeDate=" + tradeDate + ", noOfRecords=" + noOfRecords + ", grade=" + grade + "]";
	}
	
	
	
}
