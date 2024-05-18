package com.anish.data;

public class Week {
	
	private Integer id;
	private Integer weekNo;
	private String personName;
	private Integer gradeNo;
	private String wordName;
	private Boolean hasSound;
	private String meening;
	private Boolean enable;
	
	private Integer totalWeeks;
	private Integer totalWords;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Integer getGradeNo() {
		return gradeNo;
	}
	public void setGradeNo(Integer gradeNo) {
		this.gradeNo = gradeNo;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	public Boolean getHasSound() {
		return hasSound;
	}
	public void setHasSound(Boolean hasSound) {
		this.hasSound = hasSound;
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
	
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	@Override
	public String toString() {
		return "Week [id=" + id + ", weekNo=" + weekNo + ", personName=" + personName + ", gradeNo=" + gradeNo
				+ ", wordName=" + wordName + ", hasSound=" + hasSound + ", meening=" + meening + ", enable=" + enable
				+ ", totalWeeks=" + totalWeeks + ", totalWords=" + totalWords + "]";
	}
	

}