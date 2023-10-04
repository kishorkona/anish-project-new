package com.anish.entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "subjects_details_by_date")
public class SubjectsDetailsByDatePk implements Serializable {
	@Column(name = "Id")
    private Integer id;

    @Column(name = "test_date")
    private Date test_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTest_date() {
		return test_date;
	}

	public void setTest_date(Date test_date) {
		this.test_date = test_date;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		SubjectsDetailsByDatePk pk = (SubjectsDetailsByDatePk) obj;
		return (pk.id==this.id && pk.test_date.equals(this.test_date));
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result =1;
		result = PRIME * result + this.id;
		return result;
	}

	@Override
	public String toString() {
		return "SubjectsDetailsByDatePk [id=" + id + ", test_date=" + test_date + "]";
	}
    
    
}