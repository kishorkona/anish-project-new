package com.anish;

public class Emp {
	private Integer empId;
	private String empName;
	private Integer age;
	
	public Emp(Integer empId, String empName, Integer age) {
		this.empId = empId;
		this.empName = empName;
		this.age = age;
	}
	
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Emp [empId=" + empId + ", empName=" + empName + ", age=" + age + "]";
	}
}
