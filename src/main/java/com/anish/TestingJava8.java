package com.anish;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestingJava8 {
	public static void main(String[] args) {
		TestingJava8 obj = new TestingJava8();	

		String val = null;
		System.out.println(val.toLowerCase());
		//Exception in thread "main" java.lang.NullPointerException
		
		Optional<String> optional = Optional.ofNullable(val);
		if(optional.isPresent()) {
			System.out.println(val.toLowerCase());
		} else {
			System.out.println("value is null");
		}
		
		//obj.test();
	}
	private void test() {
		List<Emp> empList = getEmployees();
		// modify empList This will increate of employees+5
		
		List<Emp> finalList1 = empList.stream()
				.map(x -> {
					x.setAge(x.getAge()+5);
					return x;
				})
				.filter(x -> {
					if(x.getEmpName().equals("anish")) {
						return false;
					}
					return true;
				})
				.collect(Collectors.toList());
	}
	private List<Emp> getEmployees() {
		List<Emp> empList = new ArrayList();
		empList.add(new Emp(100, "kishor", 30));
		empList.add(new Emp(101, "sreedevi", 31));
		empList.add(new Emp(102, "anish", 32));
		empList.add(new Emp(103, "ishant", 33));
		empList.add(new Emp(104, "vijay", 34));
		return empList;
	}
}
