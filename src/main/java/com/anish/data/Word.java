package com.anish.data;

public class Word {
	
	private Integer id;
	private String name;
	private String repeat;
	private Integer totalWords;
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
	@Override
	public String toString() {
		return "Word [id=" + id + ", name=" + name + ", repeat=" + repeat + ", totalWords=" + totalWords + "]";
	}
	
	

}