package com.nt.model;

import lombok.Data;

//@Data
public class Employee {

	private Integer eId;
	private String eName;
	private String eAddrs;
	private Float salary;
	private Float netSalary;
	private Float grossSalary;
	@Override
	public String toString() {
		return "Employee [eId=" + eId + ", eName=" + eName + ", eAddrs=" + eAddrs + ", salary=" + salary
				+ ", netSalary=" + netSalary + ", grossSalary=" + grossSalary + "]";
	}
	public Integer geteId() {
		return eId;
	}
	public void seteId(Integer eId) {
		this.eId = eId;
	}
	public String geteName() {
		return eName;
	}
	public void seteName(String eName) {
		this.eName = eName;
	}
	public String geteAddrs() {
		return eAddrs;
	}
	public void seteAddrs(String eAddrs) {
		this.eAddrs = eAddrs;
	}
	public Float getSalary() {
		return salary;
	}
	public void setSalary(Float salary) {
		this.salary = salary;
	}
	public Float getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(Float netSalary) {
		this.netSalary = netSalary;
	}
	public Float getGrossSalary() {
		return grossSalary;
	}
	public void setGrossSalary(Float grossSalary) {
		this.grossSalary = grossSalary;
	}
	
}
