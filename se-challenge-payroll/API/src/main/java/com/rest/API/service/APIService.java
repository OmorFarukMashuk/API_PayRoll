package com.rest.API.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rest.API.model.ReportVersion;
import com.rest.API.model.TimeReport;
import com.rest.API.repository.ReportVersionRepository;
import com.rest.API.repository.TimeReportRepository;
import com.rest.API.utils.EmployeeReport;
import com.rest.API.utils.PayPeriodReport;
import com.rest.API.utils.Payroll;
import com.rest.API.utils.PayrollReport;

@Service
public class APIService {

	@Autowired
	TimeReportRepository repo;
	
	@Autowired
	ReportVersionRepository versionRepo;
	
	public void saveEntryToDatabase(String date, String hrsWorked, String empID, String jbGrp) throws ParseException {

		TimeReport tr = new TimeReport();
		tr.date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		tr.setHoursWorked(Double.parseDouble(hrsWorked));
		tr.setEmployeeId(Integer.parseInt(empID));
		tr.setJobGroup(jbGrp.charAt(0));
		
		repo.save(tr);
			

	}

	
	public void readCSV(MultipartFile csvFile) {

		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";

		//timeReports = new ArrayList<TimeReport>();
		try {
            InputStream is = csvFile.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			line = br.readLine();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] ln = line.split(csvSplitBy);
				
				// Need to save in MySQL
				saveEntryToDatabase(ln[0], ln[1], ln[2], ln[3]);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public String generateKey(char keyPart1, Date workDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(workDate);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		int keyPart2;

		if (day >= 1 && day <= 15) {
			keyPart2 = 1;
		} else
			keyPart2 = 16;

		String hKey = String.valueOf(keyPart1) + "_" + String.valueOf(keyPart2) + "_" + String.valueOf(month) + "_"
				+ String.valueOf(year);
		return hKey;

	}
	public Payroll generateReports(List<TimeReport> timeReports) {

		Set<Integer> empSet = new HashSet<Integer>();

		for (int i = 0; i < timeReports.size(); i++)
			empSet.add(timeReports.get(i).getEmployeeId());

		List<Integer> empList = new ArrayList<Integer>(empSet);
		Collections.sort(empList);
		
		List <EmployeeReport>employeeReports = new ArrayList<EmployeeReport>();


		// iterate over all employee
		for (int eid : empList) {
			// creating HashMap for work hours for each employee
			HashMap<String, Double> payMap = new HashMap<String, Double>();
			for (int i = 0; i < timeReports.size(); i++) {
				if (eid == timeReports.get(i).getEmployeeId()) {

					String hashKey = generateKey(timeReports.get(i).getJobGroup(), timeReports.get(i).date);

					if (payMap.containsKey(hashKey)) {
						payMap.put(hashKey, payMap.get(hashKey) + timeReports.get(i).getHoursWorked());
					} else
						payMap.put(hashKey, timeReports.get(i).getHoursWorked());

				}

			}
			
			ArrayList<PayPeriodReport> payPeriodReports;

			PayPeriodReport ppr = new PayPeriodReport();
			payPeriodReports = ppr.getPayPeriodReports(payMap, eid);
			payPeriodReports.sort((o1, o2) -> o1.getPayPeriod().startDate.compareTo(o2.getPayPeriod().startDate));
			

			EmployeeReport er = new EmployeeReport();
			ArrayList<EmployeeReport> tempER = er.getEmployeeReport(payPeriodReports, eid);

			for (EmployeeReport report : tempER)
				employeeReports.add(report);

		}
		
		PayrollReport payrollReport = new PayrollReport();
		
		payrollReport.setEmployeeReport(employeeReports);
		
		Payroll payroll = new Payroll();
		
		payroll.setPayrollReport(payrollReport);
		
		
		return payroll;


	}

	public void validate(String csvfileName) throws NumberFormatException{
		// TODO Auto-generated method stub
		
		String versionString = StringUtils.substringBetween(csvfileName, "time-report-", ".csv");
		int version;
		try {
			
			version = Integer.parseInt(versionString);
			
		} catch (Exception e) {
			// TODO: handle exception
			
			throw new NumberFormatException("Wrong file format");
			
		}
		
		Optional<ReportVersion> reportId = versionRepo.findById(version);
		
		if(reportId.isPresent()) {
			throw new NumberFormatException("File already exists");
		}
		else {
			ReportVersion report = new ReportVersion();
			report.setReportId(version);
			versionRepo.save(report);
		}

		
	}


	public void deleteEmployeeReport(List<TimeReport> timeReports) {
		// TODO Auto-generated method stub
		
		for(TimeReport tr : timeReports) {
			repo.deleteById(tr.getId());
		}
		return;

	}


}
