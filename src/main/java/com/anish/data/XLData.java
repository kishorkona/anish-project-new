package com.anish.data;

public class XLData {
	private Integer questionId;
	private String questionName;
	
	private String questionFileName;
	private String questionCompletedFileName;
	
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestionFileName() {
		return questionFileName;
	}
	public void setQuestionFileName(String questionFileName) {
		this.questionFileName = questionFileName;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionCompletedFileName() {
		return questionCompletedFileName;
	}
	public void setQuestionCompletedFileName(String questionCompletedFileName) {
		this.questionCompletedFileName = questionCompletedFileName;
	}
	@Override
	public String toString() {
		return "XLData [questionId=" + questionId + ", questionFileName=" + questionFileName + ", questionName="
				+ questionName + ", questionCompletedFileName=" + questionCompletedFileName + "]";
	}
	
}
