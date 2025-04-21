package com.iot.project.com.iot.project.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.error.ErrorResponse;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;

import jakarta.annotation.PostConstruct;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private final AppProperties appProperties;
    private String APP_NAME; // constante de instancia final (efectiva)

    public GlobalExceptionHandler(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @PostConstruct
    public void init() {
        this.APP_NAME = appProperties.getResponseKey();
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(ex.getStatus())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ApiKeyAlreadyExistsException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleApiKeyAlreadyExistsException(ApiKeyAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(ex.getStatus())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .status(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(ex.getCode())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorsMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("Field '%s': %s",
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("INVALID_REQUEST")
                .message(errorsMessage)
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String userFriendlyMessage = "The request body could not be read. Make sure you are sending valid JSON and the fields are correct.";
       
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("INVALID_REQUEST")
                .message(userFriendlyMessage)
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ServiceResponse<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException ex) {
        String userFriendlyMessage = "The request body could not be read. Make sure you are sending valid JSON and the fields are correct.";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("INVALID_REQUEST")
                .message(userFriendlyMessage)
                .build();
        return new ResponseEntity<>(new ServiceResponse<>(APP_NAME,ActionMethod.ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }

}




//     @ExceptionHandler(UnauthorizedException.class)
//     public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code(ex.getCode())
//                 .message(ex.getMessage())
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//     }

//     @ExceptionHandler(BadRequestException.class)
//     public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code(ex.getCode())
//                 .message(ex.getMessage())
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//     }

//     @ExceptionHandler(NotFoundException.class)
//     public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code(ex.getCode())
//                 .message(ex.getMessage())
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//     }

//     @ExceptionHandler(ApiKeyAlreadyExistsException.class)
//     public ResponseEntity<ErrorResponse> handleApiKeyAlreadyExistsException(ApiKeyAlreadyExistsException ex) {
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code(ex.getCode())
//                 .message(ex.getMessage())
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//     }

//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//         BindingResult bindingResult = ex.getBindingResult();

//         String errorsMessage = bindingResult.getFieldErrors().stream()
//                 .map(fieldError -> String.format("Field '%s': %s",
//                         fieldError.getField(),
//                         fieldError.getDefaultMessage()))
//                 .collect(Collectors.joining(", "));

//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code("INVALID_REQUEST")
//                 .message(errorsMessage)
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//     }

//     @ExceptionHandler(HttpMessageNotReadableException.class)
//     public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

//         String userFriendlyMessage = "The request body could not be read. Make sure you are sending valid JSON and the fields are correct.";
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code("INVALID_REQUEST")
//                 .message(userFriendlyMessage)
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//     }

//     @ExceptionHandler(NoResourceFoundException.class)
//     public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
//         String userFriendlyMessage = "The request body could not be read. Make sure you are sending valid JSON and the fields are correct.";
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .code("INVALID_REQUEST")
//                 .message(userFriendlyMessage)
//                 .build();
//         return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//     }
// }
