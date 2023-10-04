package com.anish.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subjects_details")
public class SubjectsDetails {
	
	@Id
    @Column(name = "Id", nullable = false, unique = true)
	private int id;
	
	@Column(name = "DetailId", nullable = true)
	private int detailId;
	
	@Column(name = "subject_id", nullable = false)
	private int subjectId;
	
	@Column(name = "Url", nullable = true)
	private String url;
	
	@Column(name = "no_of_questions", nullable = true)
	private int noOfQuestions;
	
	@Column(name = "isNew", nullable = true)
	private int isNew;
	
	@Column(name = "is_active", nullable = true)
	private int isActive;
	
	@Column(name = "oldCount", nullable = true)
	private int oldCount;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getNoOfQuestions() {
		return noOfQuestions;
	}
	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}
	
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	
	public int getOldCount() {
		return oldCount;
	}
	public void setOldCount(int oldCount) {
		this.oldCount = oldCount;
	}
	@Override
	public String toString() {
		return "SubjectsDetails [id=" + id + ", detailId=" + detailId + ", subjectId=" + subjectId + ", url=" + url
				+ ", noOfQuestions=" + noOfQuestions + ", isNew=" + isNew + ", isActive=" + isActive + ", oldCount="
				+ oldCount + "]";
	}
	
}
