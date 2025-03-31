package com.iot.project.com.iot.project.TestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.CustomException;
import com.iot.project.com.iot.project.service.CompanyTestService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
	private final CompanyTestService companyService;
	@PostMapping ("/company/create/{username}/{password}")
	@Operation(description =  "")		
	public ResponseEntity<?> createCompany(@PathVariable String username, @PathVariable String password, @RequestBody Company company){
		try {
			return new ResponseEntity<>(this.companyService.create(username, password, company), HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.toString(), e.getHttpStatus());		
		}
	}	

	@GetMapping({"/company/id/{id}", "/company/apiKey/{apiKey}"})
	@Operation(description =  "")		
	public ResponseEntity<?> getCompany(@PathVariable(required = false) Long id, @PathVariable(required = false) String apiKey ){
		try {
			if(id != null) {
				return new ResponseEntity<>(this.companyService.findById(id), HttpStatus.OK);
			}
			return new ResponseEntity<>(this.companyService.findByApiKeyId(apiKey), HttpStatus.OK);			
		} catch (CustomException e) {
			return new ResponseEntity<>(e.toString(), e.getHttpStatus());		
		}
	}	
	
}
