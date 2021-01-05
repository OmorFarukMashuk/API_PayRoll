package com.rest.API.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayPeriodReport {
	
	private PayPeriod payPeriod;	
	private double totalHours;
	private double amountPaid;
	
	
	// Getter
	public PayPeriod getPayPeriod() {
		return payPeriod;
	}
	public double getTotalHours() {
		return totalHours;
	}
	public double getAmountPaid() {
		return amountPaid;
	}

	// Setter
	public void setPayPeriod(PayPeriod payPeriod) {
		this.payPeriod = payPeriod;
	}
	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	

	public ArrayList<PayPeriodReport> getPayPeriodReports(HashMap<String, Double> payMap, int empID) {
		
		
		ArrayList<PayPeriodReport> payPeriodReports;
		PayPeriodReport ppr;
		
		
		payPeriodReports = new ArrayList<PayPeriodReport>();
		
		
		for (String key : payMap.keySet()) {
			
			ppr = new PayPeriodReport();
			Matcher matcher = Pattern.compile("\\d+").matcher(key);
			List<Integer> list = new ArrayList<Integer>();
			while (matcher.find()) {
				list.add(Integer.parseInt(matcher.group()));
			}
			String startDay = String.valueOf(list.get(0));
			String month = String.valueOf(list.get(1));
			String year = String.valueOf(list.get(2));
			String endDay;

				
			try {
				
				PayPeriod pp = new PayPeriod();
				pp.startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDay + "/" + month + "/" + year);
				if (list.get(0) == 1) {
					endDay = String.valueOf(15);
				} else {
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(pp.startDate);
					int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					endDay = String.valueOf(day);
				}
				
				pp.endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDay + "/" + month + "/" + year);
				ppr.payPeriod = pp;


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			ppr.totalHours = payMap.get(key);
			
			if(key.charAt(0) == 'A') ppr.amountPaid = ppr.totalHours * 20;
			else ppr.amountPaid = ppr.totalHours * 30;

			payPeriodReports.add(ppr);


		}
		
		return payPeriodReports;
		


	}

}
