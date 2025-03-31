package com.iot.project.com.iot.project.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer id;
	String description;
	HttpStatus httpStatus;
	
	private static ObjectMapper MAPPER = new ObjectMapper();
	public CustomException(Integer _id, String _description) {
		super();
		this.id = _id;
		this.description = _description;
		this.setHttpStatus();
	}
	private void setHttpStatus() {
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if(this.id == ExceptionDescription.NOT_FOUND) {
			this.httpStatus = HttpStatus.NOT_FOUND;
		}
		else if(this.id == ExceptionDescription.REQUIRED_FIELDS_EXCEPTION) {
			this.httpStatus = HttpStatus.BAD_REQUEST;
		}
		else if(this.id == ExceptionDescription.ENTITY_ALREADY_EXISTS) {
			this.httpStatus = HttpStatus.BAD_REQUEST;
		}		
		else if(this.id == ExceptionDescription.TRANSACTION_OK) {
			this.httpStatus = HttpStatus.OK;
		}				
		else if(this.id == ExceptionDescription.INCORRECT_CREDENTIALS) {
			this.httpStatus = HttpStatus.NOT_FOUND;
		}						
	}	
	@Override
	public String toString(){
		try {
			return MAPPER.writeValueAsString(new ExceptionDescription(this.id, this.description));
		} catch (JsonProcessingException e) {
			return "{'id':-9999, 'description':'Unknown exception'}";
		}
	}	
	@AllArgsConstructor
	@Getter
	@Setter
	public class ExceptionDescription {
		public static Integer NOT_FOUND = -1; 
		public static Integer RUN_TIME_EXCEPTION = -2; 
		public static final Integer MAPPING_EXCEPTION = -3;		

		public static String  NOT_FOUND_MSG = "Entity not found"; 	
		public static String  RUN_TIME_EXCEPTION_MSG = "Database query failed"; 	
		public static final String MAPPING_EXCEPTION_MSG = "mapping exception";
		public static final Integer REQUIRED_FIELDS_EXCEPTION = -4;
		public static final String REQUIRED_FIELDS_EXCEPTION_MSG = "Null required fields error";
		public static final Integer UNKNOWN_EXCEPTION = -5;
		public static final String UNKNOWN_EXCEPTION_MSG = "Uncaught error";		
		public static Integer ENTITY_ALREADY_EXISTS = -6; 		
		public static String  ENTITY_ALREADY_EXISTS_MSG = "Entity already exists"; 	
		public static final Integer TRANSACTION_OK = 1;
		public static final String TRANSACTION_OK_MSG = "Transaction successfully";				
		public static final Integer TRANSACTION_NOT_OK = -10;
		public static final String TRANSACTION_NOT_OK_MSG = "Transaction error";
		public static final Integer INCORRECT_CREDENTIALS = -11;
		public static final String INCORRECT_CREDENTIALS_MSG = "Incorrect credencials or administrator not found";
		Integer id;
		String description;
	}
}
