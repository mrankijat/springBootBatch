package com.nt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {

	private Integer eId;
	private String eName;
	private String eAddrs;
	private Float salary;
	private Float netSalary;
	private Float grossSalary;
	
}
