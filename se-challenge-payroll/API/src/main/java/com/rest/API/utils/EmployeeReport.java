package com.rest.API.utils;

import java.util.ArrayList;

public class EmployeeReport {
	private int employeeID;
	private PayPeriod payPeriod;
	private String amountPaid;
	
	
	// Getter
	public int getEmployeeId() {
		return this.employeeID;
	}
	public PayPeriod getPayPeriod() {
		return payPeriod;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	// Setter
	public void setEmployeeId(int employeeID){
		this.employeeID = employeeID;
	}
	public void setPayPeriod(PayPeriod payPeriod) {
		this.payPeriod = payPeriod;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	
	public ArrayList<EmployeeReport> getEmployeeReport(ArrayList<PayPeriodReport> employeePayPeriodReports, int employeeID){
		
		ArrayList<EmployeeReport> employeeReports = new ArrayList<EmployeeReport>();
		
		for(PayPeriodReport report : employeePayPeriodReports) {
			EmployeeReport er = new EmployeeReport();
			er.employeeID = employeeID;
							
			PayPeriod pp = new PayPeriod();
			pp.startDate = report.getPayPeriod().startDate;
			pp.endDate = report.getPayPeriod().endDate;		
			er.payPeriod = pp;			
			er.amountPaid = "$" + String.valueOf(report.getAmountPaid());				
			employeeReports.add(er);
			
		}
		
		return employeeReports;
		
	}

	

}
