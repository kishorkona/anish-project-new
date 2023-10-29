package com.anish.data;

public class Week {
	
	private Integer weekNo;
	private String name;
	private String word;
	private Integer wordNo;
	private Integer gradeNo;
	private String meening;
	private String hasSound;
	private Integer totalWeeks;
	private Integer totalWords;
	

	public Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getMeening() {
		return meening;
	}
	public void setMeening(String meening) {
		this.meening = meening;
	}
	public Integer getTotalWeeks() {
		return totalWeeks;
	}
	public void setTotalWeeks(Integer totalWeeks) {
		this.totalWeeks = totalWeeks;
	}
	public Integer getTotalWords() {
		return totalWords;
	}
	public void setTotalWords(Integer totalWords) {
		this.totalWords = totalWords;
	}
	
	
	public Integer getWordNo() {
		return wordNo;
	}
	public void setWordNo(Integer wordNo) {
		this.wordNo = wordNo;
	}
	
	
	
	public Integer getGradeNo() {
		return gradeNo;
	}
	public void setGradeNo(Integer gradeNo) {
		this.gradeNo = gradeNo;
	}
	public String getHasSound() {
		return hasSound;
	}
	public void setHasSound(String hasSound) {
		this.hasSound = hasSound;
	}
	@Override
	public String toString() {
		return "Week [weekNo=" + weekNo + ", name=" + name + ", word=" + word + ", wordNo=" + wordNo + ", gradeNo=" + gradeNo
				+ ", meening=" + meening + ", hasSound=" + hasSound + ", totalWeeks=" + totalWeeks + ", totalWords="
				+ totalWords + "]";
	}

}