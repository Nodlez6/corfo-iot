package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.entity.Admin;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void testGetCompanies() {
        List<Company> companies = Arrays.asList(
                Company.builder().companyName("Company A").build(),
                Company.builder().companyName("Company B").build()
        );
        when(companyRepository.findAll()).thenReturn(companies);

        List<Company> result = companyService.getCompanies();

        assertEquals(2, result.size());
        verify(companyRepository).findAll();
    }

    @Test
    void testGetCompanyByIdFound() {
        Company company = Company.builder().companyName("Test Company").build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company result = companyService.getCompanyById(1L);

        assertNotNull(result);
        assertEquals("Test Company", result.getCompanyName());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testGetCompanyByIdNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                companyService.getCompanyById(1L)
        );

        assertEquals(RESOURCE_NOT_FOUND, thrown.getMessage());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testCreateCompany() {
        String companyName = "New Company";
        String username = "admin";
        String password = "password";

        Admin admin = Admin.builder()
                .username(username)
                .password(password)
                .build();

        Company savedCompany = Company.builder().companyName(companyName).build();
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        Company result = companyService.createCompany(companyName, admin);

        assertNotNull(result);
        assertEquals(companyName, result.getCompanyName());
        verify(adminService).authenticateAdmin(username, password);
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void testGetCompanyByApiKeyFound() {
        UUID apiKey = UUID.randomUUID();
        Company company = Company.builder().companyName("Test Company").build();
        when(companyRepository.findByCompanyApiKey(apiKey)).thenReturn(Optional.of(company));

        Company result = companyService.getCompanyByApiKey(apiKey);

        assertNotNull(result);
        verify(companyRepository).findByCompanyApiKey(apiKey);
    }

    @Test
    void testGetCompanyByApiKeyNotFound() {
        UUID apiKey = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(apiKey)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                companyService.getCompanyByApiKey(apiKey)
        );

        assertEquals(RESOURCE_NOT_FOUND, thrown.getMessage());
        verify(companyRepository).findByCompanyApiKey(apiKey);
    }

    @Test
    void testUpdateCompany() {
        Long id = 1L;
        String newCompanyName = "Updated Company";
        UUID newApiKey = UUID.randomUUID();
        String username = "admin";
        String password = "password";

        Admin admin = Admin.builder().username(username).password(password).build();
        Company existingCompany = Company.builder().companyName("Old Company").build();

        when(companyRepository.findById(id)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);

        Company result = companyService.updateCompany(id, newCompanyName, newApiKey, admin);

        assertNotNull(result);
        assertEquals(newCompanyName, result.getCompanyName());
        verify(adminService).authenticateAdmin(username, password);
        verify(companyRepository).findById(id);
        verify(companyRepository).save(existingCompany);
    }

    @Test
    void testExistsCompanyApiKeyTrue() {
        UUID apiKey = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(apiKey))
                .thenReturn(Optional.of(Company.builder().companyName("Test Company").build()));

        boolean exists = companyService.existsCompanyApiKey(apiKey);

        assertTrue(exists);
        verify(companyRepository).findByCompanyApiKey(apiKey);
    }

    @Test
    void testExistsCompanyApiKey_False() {
        UUID apiKey = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(apiKey))
                .thenReturn(Optional.empty());

        boolean exists = companyService.existsCompanyApiKey(apiKey);

        assertFalse(exists);
        verify(companyRepository).findByCompanyApiKey(apiKey);
    }

    @Test
    void testDeleteCompanyById_Success() {
        Long id = 1L;
        Company company = Company.builder().companyName("Company to delete").build();
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        companyService.deleteCompanyById(id);
        verify(companyRepository).findById(id);
        verify(companyRepository).delete(company);
    }

    @Test
    void testDeleteCompanyByIdNotFound() {
        Long id = 1L;
        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
                companyService.deleteCompanyById(id)
        );

        assertEquals(RESOURCE_NOT_FOUND, thrown.getMessage());
        verify(companyRepository).findById(id);
    }
}
