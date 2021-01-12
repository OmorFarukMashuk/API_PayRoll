package com.rest.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.API.model.TimeReport;

@Repository
public interface TimeReportRepository extends JpaRepository<TimeReport, Integer> {
	
	public List<TimeReport> findAllByEmployeeID(int employeeID);

	

}
