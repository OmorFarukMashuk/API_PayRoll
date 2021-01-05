package com.rest.API.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rest.API.config.MapperConfig;
import com.rest.API.model.TimeReport;
import com.rest.API.repository.TimeReportRepository;
import com.rest.API.service.APIService;
import com.rest.API.utils.Payroll;

@RestController
public class TimeReportController {

	@Autowired
	TimeReportRepository timeReportRepository;
	
	@Autowired
	MapperConfig mapperConfig;
	
	@Autowired
	APIService apiService;
	
	// Upload csv
	@PostMapping("/add")
	public ResponseEntity<String> addTimeReport(@RequestParam("file") MultipartFile file){
			
		String csvfileName = file.getOriginalFilename();
		
		try {
			apiService.validate(csvfileName);

			
		} catch (Exception e) {
			// TODO: handle exception
			
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		apiService.readCSV(file);
		
		return new ResponseEntity<String>("Success", HttpStatus.OK); 
	}
	
	// Return obj
	@GetMapping("/report")
	public String getTimeReport() throws JsonProcessingException {
		
		
		List<TimeReport> timeReports = timeReportRepository.findAll();
		
		Payroll payroll = apiService.generateReports(timeReports);
		
		
		ObjectWriter ow = mapperConfig.objectMapper().writer().withDefaultPrettyPrinter();
		
		
		String json = ow.writeValueAsString(payroll);
		return json;
	}
}
