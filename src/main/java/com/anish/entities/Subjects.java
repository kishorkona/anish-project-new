package com.anish.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "subjects")
@NamedNativeQuery(name = "findByCode", query = "SELECT * FROM subjects WHERE code=?", resultClass = Subjects.class)
public class Subjects {
	private int id;
	private int userId;
	private int gradeId;
	private int subject_order;
	private String code;
	private String name;
	private int subject;
	private int isActive;
	private int noOfQuestions;
	
	@Id
    @Column(name = "Id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "user_id", nullable = true)
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Column(name = "grade_id", nullable = true)
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	@Column(name = "subject_order", nullable = true)
	public int getSubject_order() {
		return subject_order;
	}
	public void setSubject_order(int subject_order) {
		this.subject_order = subject_order;
	}
	
	@Column(name = "Code", nullable = true)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "Name", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "Subject", nullable = false)
	public int getSubject() {
		return subject;
	}
	public void setSubject(int subject) {
		this.subject = subject;
	}
	
	@Column(name = "is_active", nullable = true)
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	@Column(name = "no_of_questions", nullable = true)
	public int getNoOfQuestions() {
		return noOfQuestions;
	}
	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}
	@Override
	public String toString() {
		return "Subjects [id=" + id + ", userId=" + userId + ", gradeId=" + gradeId + ", subject_order=" + subject_order + ", code=" + code
				+ ", name=" + name + ", subject=" + subject + ", isActive=" + isActive + ", noOfQuestions="
				+ noOfQuestions + "]";
	}
	
	
	
}
