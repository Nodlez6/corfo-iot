package com.iot.project.com.iot.project.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.entity.Company;

import jakarta.transaction.Transactional;



@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	Optional<Admin> findByUsername(String username);
	Optional<Admin> findByUsernameAndPassword(String username, String username2);


}


