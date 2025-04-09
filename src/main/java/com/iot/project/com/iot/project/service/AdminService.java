package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.INVALID_CREDENTIALS;

@Service
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void authenticateAdmin(String username, String password){
        adminRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new NotFoundException(INVALID_CREDENTIALS));
    }
}
