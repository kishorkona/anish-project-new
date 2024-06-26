package com.anish.data;

public class Word {
	
	private Integer id;
	private String name;
	private Integer fileNo;
	private String repeat;
	private Integer totalWords;
	private boolean hasSentence;
	private String sentence;
	private String combination;
	private String testName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public Integer getTotalWords() {
		return totalWords;
	}
	public void setTotalWords(Integer totalWords) {
		this.totalWords = totalWords;
	}
	
	public boolean isHasSentence() {
		return hasSentence;
	}
	public void setHasSentence(boolean hasSentence) {
		this.hasSentence = hasSentence;
	}
	public Integer getFileNo() {
		return fileNo;
	}
	public void setFileNo(Integer fileNo) {
		this.fileNo = fileNo;
	}
	
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getCombination() {
		return combination;
	}
	public void setCombination(String combination) {
		this.combination = combination;
	}
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	@Override
	public String toString() {
		return "Word [id=" + id + ", name=" + name + ", fileNo=" + fileNo + ", repeat=" + repeat + ", totalWords="
				+ totalWords + ", hasSentence=" + hasSentence + ", sentence=" + sentence + ", combination="
				+ combination + ", testName=" + testName + "]";
	}
	
}