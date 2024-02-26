package com.anish.data;

public class Emp {
	private String empName;
	private Integer empId;
	private Integer age;
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Emp [empName=" + empName + ", empId=" + empId + ", age=" + age + "]";
	}
	
	
	
}
