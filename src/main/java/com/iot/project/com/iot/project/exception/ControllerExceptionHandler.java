package com.iot.project.com.iot.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.iot.project.com.iot.project.exception.CustomException.ExceptionDescription;


@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<?> resourceNotFoundException(NoResourceFoundException ex, WebRequest request) {
	  CustomException _customException = new CustomException( ExceptionDescription.NOT_FOUND, ex.getMessage());
	  return new ResponseEntity<>(_customException.toString(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> globalExceptionHandler(RuntimeException ex, WebRequest request) {

	  CustomException _customException = new CustomException( ExceptionDescription.TRANSACTION_NOT_OK, ex.getMessage());
    
	  return new ResponseEntity<>(_customException.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
