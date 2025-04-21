package com.iot.project.com.iot.project.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Operations related to administrative functionalities such as password management and access control.")
public class AdminController {

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Public access endpoint", description = "This is a public endpoint that does not require authentication.", responses = {
            @ApiResponse(responseCode = "200", description = "Public endpoint accessed successfully")
    })
    @GetMapping("/public")
    public ResponseEntity<String> publicAccess() {
        System.out.println("PUBLIC");
        return ResponseEntity.ok("This endpoint is public");
    }

    @Operation(summary = "Secure access endpoint", description = "This endpoint is protected and accessible only by users with the ADMIN role.", responses = {
            @ApiResponse(responseCode = "200", description = "Access granted to ADMIN"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/secure")
    public ResponseEntity<String> secureAccess() {
        return ResponseEntity.ok("Hello ADMIN - You have access!");
    }

    @Operation(summary = "Encode password", description = "Encodes a raw password using the configured PasswordEncoder.", responses = {
            @ApiResponse(responseCode = "200", description = "Password encoded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password input")
    })
    @PostMapping("/encode")
    public ResponseEntity<String> encodePassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String encoded = passwordEncoder.encode(rawPassword);
        return ResponseEntity.ok(encoded);
    }

    @Operation(summary = "Match password with encoded value", description = "Compares a raw password with an encoded password to check if they match.", responses = {
            @ApiResponse(responseCode = "200", description = "Passwords match"),
            @ApiResponse(responseCode = "401", description = "Passwords do not match")
    })
    @PostMapping("/decode")
    public ResponseEntity<String> matchPassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String encodedPassword = request.get("encodedPassword");

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        if (matches) {
            return ResponseEntity.ok("Passwords match!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Passwords do NOT match.");
        }
    }
}