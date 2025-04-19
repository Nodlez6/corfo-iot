package com.iot.project.com.iot.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.iot.project.com.iot.project.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByCompanyApiKey(UUID apiIKey);

	Optional<Company> findByCompanyId(Long id);

	List<Company> findByCompanyName(String _name);
}
