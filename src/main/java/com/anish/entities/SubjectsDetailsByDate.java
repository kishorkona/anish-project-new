package com.anish.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subjects_details_by_date")
public class SubjectsDetailsByDate {
	
	@EmbeddedId
	private SubjectsDetailsByDatePk subjectsDetailsByDatePk;
	
	@Column(name = "completed_date", nullable = true)
	private Timestamp completedDate;
	
	@Column(name = "Url", nullable = true)
	private String url;
	
	@Column(name = "Code", nullable = true)
	private String code;
	
	@Column(name = "detail_id", nullable = true)
	private int detailId;
	
	@Column(name = "grade_id", nullable = true)
	private int gradeId;
	
	public SubjectsDetailsByDatePk getSubjectsDetailsByDatePk() {
		return subjectsDetailsByDatePk;
	}
	public void setSubjectsDetailsByDatePk(SubjectsDetailsByDatePk subjectsDetailsByDatePk) {
		this.subjectsDetailsByDatePk = subjectsDetailsByDatePk;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Timestamp getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Timestamp completedDate) {
		this.completedDate = completedDate;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	@Override
	public String toString() {
		return "SubjectsDetailsByDate [subjectsDetailsByDatePk=" + subjectsDetailsByDatePk + ", completedDate="
				+ completedDate + ", url=" + url + ", code=" + code + ", detailId=" + detailId + ", gradeId=" + gradeId
				+ "]";
	}
	
	
	
	
	
}
