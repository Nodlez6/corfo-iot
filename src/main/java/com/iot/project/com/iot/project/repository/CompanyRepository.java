package com.iot.project.com.iot.project.repository;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Company;

import jakarta.transaction.Transactional;



@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByApiKey(UUID apiIKey);
	List<Company> findByName(String _name);
}


