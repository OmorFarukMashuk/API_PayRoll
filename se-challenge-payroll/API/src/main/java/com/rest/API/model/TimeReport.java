package com.rest.API.model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "employee")
public class TimeReport {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonIgnore
	public int id;
	public Date date;
	private double hoursWorked;
	private int employeeID;
	private char jobGroup;

	// Getter

	public int getId() {
		return this.id;
	}

	public Date getDate() {
		return date;
	}

	public double getHoursWorked() {
		return hoursWorked;
	}

	public int getEmployeeId() {
		return employeeID;
	}

	public char getJobGroup() {
		return jobGroup;
	}

	// Setter
	public void setID(int id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setHoursWorked(double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public void setEmployeeId(int employeeID) {
		this.employeeID = employeeID;
	}

	public void setJobGroup(char jobGroup) {
		this.jobGroup = jobGroup;
	}
}
