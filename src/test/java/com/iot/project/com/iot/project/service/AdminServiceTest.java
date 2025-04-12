package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.INVALID_CREDENTIALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;


    @Test
    void authenticateAdminSuccess() {
        String username = "admin";
        String password = "password";

        Admin admin = Admin.builder()
                .username(username)
                .password(password)
                .build();

        when(adminRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(admin));

        adminService.authenticateAdmin(username, password);

        verify(adminRepository).findByUsernameAndPassword(username, password);
    }

    @Test
    void authenticateAdminInvalidCredentialsThrowsNotFoundException() {
        String username = "admin";
        String password = "wrongPassword";

        when(adminRepository.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                adminService.authenticateAdmin(username, password)
        );
        assertEquals(INVALID_CREDENTIALS, exception.getMessage());
        verify(adminRepository).findByUsernameAndPassword(username, password);
    }
}
