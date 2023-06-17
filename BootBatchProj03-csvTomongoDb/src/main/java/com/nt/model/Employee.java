package com.nt.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collation = "Employee_Info")
public class Employee {

	private Integer eId;
	private String eName;
	private String eAddrs;
	private Float salary;
	private Float netSalary;
	private Float grossSalary;

}
