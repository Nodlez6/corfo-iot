package com.iot.project.com.iot.project.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminController controller;

    @Test
    void publicAccessShouldReturnOk() {
        ResponseEntity<String> response = controller.publicAccess();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This endpoint is public", response.getBody());
    }

    @Test
    void secureAccessShouldReturnOk() {
        ResponseEntity<String> response = controller.secureAccess();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello ADMIN - You have access!", response.getBody());
    }

    @Test
    void encodePasswordShouldReturnEncodedValue() {
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");
        Map<String,String> request = Map.of("password", "rawPass");
        ResponseEntity<String> response = controller.encodePassword(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("encodedPass", response.getBody());
    }

    @Test
    void matchPasswordShouldReturnOkWhenPasswordsMatch() {
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        Map<String,String> request = Map.of(
                "password", "rawPass",
                "encodedPassword", "encodedPass"
        );
        ResponseEntity<String> response = controller.matchPassword(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Passwords match!", response.getBody());
    }

    @Test
    void matchPasswordShouldReturnUnauthorizedWhenPasswordsDoNotMatch() {
        when(passwordEncoder.matches("rawPass", "wrongPass")).thenReturn(false);
        Map<String,String> request = Map.of(
                "password", "rawPass",
                "encodedPassword", "wrongPass"
        );
        ResponseEntity<String> response = controller.matchPassword(request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Passwords do NOT match.", response.getBody());
    }
}
