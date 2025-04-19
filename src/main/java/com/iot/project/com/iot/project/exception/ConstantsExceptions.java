package com.iot.project.com.iot.project.exception;

public class ConstantsExceptions {



    //COMPANY
    public static final String COMPANY_ID_NOT_FOUND = "Company with the given 'id' was not found.";
    public static final String COMPANY_APIKEY_NOT_FOUND = "Company with the given 'apiKey' was not found.";
    public static final String API_KEY_ALREADY_EXISTS = "The provided API key is already associated with an existing company. Please use a unique API key.";

    //LOCATION
    public static final String LOCATION_ID_NOT_FOUND = "Location with the given 'id' was not found.";
    public static final String BAD_REQUEST_LOCATION_SERVICE = "Bad request. Please verify your input data. The provided company API key does not match the location.";
    public static final String LOCATION_NOT_FOUND_FOR_COMPANY = "Location not found for authenticated company";

    //SENSOR
    public static final String SENSOR_NOT_FOUND_BY_COMPANY = "Sensor not found for authenticated company";
    public static final String LOCATION_ID_NOT_FOUND_BY_COMPANY = "Location id not found for authenticated company";
    public static final String SENSOR_APIKEY_NOT_FOUND = "Sensor with the given 'apiKey' was not found.";

    
    public static final String ERROR = "error";
    public static final String BLANK_USER_PASS = "Field 'user' and 'password' must not be blank";
    public static final String BLANK_USER = "Field 'user' must not be blank";
    public static final String BLANK_PASS = "Field 'password' must not be blank";
    




    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String ENTITY_NOT_FOUND_BY_COMPANY = "not found for authenticated company";
    public static final String INVALID_SENSOR_API_KEY = "No sensor exists with the provided API key.";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access. You do not have permission to access this resource.";
    public static final String SERVER_ERROR = "An internal server error occurred. Please try again later.";
    public static final String BAD_REQUEST = "Bad request. Please verify your input data.";
    public static final String DATA_NOT_FOUND = "The requested data could not be found.";
    public static final String DUPLICATE_ENTRY = "Duplicate entry detected. The record already exists.";
}
