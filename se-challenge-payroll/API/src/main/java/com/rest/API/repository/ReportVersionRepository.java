package com.rest.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.API.model.ReportVersion;

@Repository
public interface ReportVersionRepository extends JpaRepository<ReportVersion, Integer>{

}
