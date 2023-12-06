package com.anish.data;

public class Question {
	
	private String subject;
	private String grade;
	private String sectionId;
	private String sectionName;
	private Integer questionStatus;
	private String subSectionId;
	private String subSectionValue;
	private String subSectionName;
	
	private Integer totalCurrentQuestions;
	private Integer totalComplexQuestions;
	private Integer totalCompletedQuestions;
	private Integer id;
	private String url;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Integer getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}
	public String getSubSectionId() {
		return subSectionId;
	}
	public void setSubSectionId(String subSectionId) {
		this.subSectionId = subSectionId;
	}
	public String getSubSectionName() {
		return subSectionName;
	}
	public void setSubSectionName(String subSectionName) {
		this.subSectionName = subSectionName;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getTotalCurrentQuestions() {
		return totalCurrentQuestions;
	}
	public void setTotalCurrentQuestions(Integer totalCurrentQuestions) {
		this.totalCurrentQuestions = totalCurrentQuestions;
	}
	public Integer getTotalComplexQuestions() {
		return totalComplexQuestions;
	}
	public void setTotalComplexQuestions(Integer totalComplexQuestions) {
		this.totalComplexQuestions = totalComplexQuestions;
	}
	public Integer getTotalCompletedQuestions() {
		return totalCompletedQuestions;
	}
	public void setTotalCompletedQuestions(Integer totalCompletedQuestions) {
		this.totalCompletedQuestions = totalCompletedQuestions;
	}
	
	public String getSubSectionValue() {
		return subSectionValue;
	}
	public void setSubSectionValue(String subSectionValue) {
		this.subSectionValue = subSectionValue;
	}
	@Override
	public String toString() {
		return "Question [subject=" + subject + ", grade=" + grade + ", sectionId=" + sectionId + ", sectionName="
				+ sectionName + ", questionStatus=" + questionStatus + ", subSectionId=" + subSectionId
				+ ", subSectionValue=" + subSectionValue + ", subSectionName=" + subSectionName
				+ ", totalCurrentQuestions=" + totalCurrentQuestions + ", totalComplexQuestions="
				+ totalComplexQuestions + ", totalCompletedQuestions=" + totalCompletedQuestions + ", id=" + id
				+ ", url=" + url + "]";
	}

}