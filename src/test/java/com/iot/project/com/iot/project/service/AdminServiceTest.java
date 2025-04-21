package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.INVALID_CREDENTIALS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private final String USERNAME = "adminUser";
    private final String PASSWORD = "securePass";

    @Test
    void authenticateAdminDoesNotThrowWhenCredentialsAreValid() {
        when(adminRepository.findByUsernameAndPassword(USERNAME, PASSWORD))
                .thenReturn(Optional.of(mock(Admin.class)));
        assertDoesNotThrow(() -> adminService.authenticateAdmin(USERNAME, PASSWORD));
        verify(adminRepository, times(1))
                .findByUsernameAndPassword(USERNAME, PASSWORD);
    }

    @Test
    void authenticateAdminThrowsNotFoundExceptionWhenCredentialsAreInvalid() {
        when(adminRepository.findByUsernameAndPassword(USERNAME, PASSWORD))
                .thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> adminService.authenticateAdmin(USERNAME, PASSWORD)
        );
        assertEquals(INVALID_CREDENTIALS, exception.getMessage());
        verify(adminRepository, times(1))
                .findByUsernameAndPassword(USERNAME, PASSWORD);
    }
}
