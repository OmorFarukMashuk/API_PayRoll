package com.rest.API.repository;

import com.rest.API.model.TimeReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeReportRepository extends JpaRepository<TimeReport, Integer> {
}
