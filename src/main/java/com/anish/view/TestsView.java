package com.anish.view;

import java.sql.Date;

public class TestsView {
	private String url;
	private String code;
	private Integer id;
    private Date testDate;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTestDate() {
		return testDate;
	}
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	@Override
	public String toString() {
		return "TestsView [url=" + url + ", code=" + code + ", id=" + id + ", testDate=" + testDate + "]";
	}
    
    
    
}
