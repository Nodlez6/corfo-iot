package com.iot.project.com.iot.project.service;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.ApiKeyAlreadyExistsException;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void getCompaniesReturnsAllCompanies() {
        List<Company> list = List.of(
                Company.builder().companyId(1L).companyName("A").companyApiKey(UUID.randomUUID()).build(),
                Company.builder().companyId(2L).companyName("B").companyApiKey(UUID.randomUUID()).build()
        );
        when(companyRepository.findAll()).thenReturn(list);
        assertEquals(list, companyService.getCompanies());
    }

    @Test
    void getCompanyByIdReturnsCompanyWhenExists() {
        Company c = Company.builder().companyId(1L).build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(c));
        assertEquals(c, companyService.getCompanyById(1L));
    }

    @Test
    void getCompanyByIdThrowsNotFoundExceptionWhenNotExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> companyService.getCompanyById(1L));
        assertEquals(COMPANY_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void getCompanyByApiKeyReturnsCompanyWhenExists() {
        UUID key = UUID.randomUUID();
        Company c = Company.builder().companyApiKey(key).build();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.of(c));
        assertEquals(c, companyService.getCompanyByApiKey(key));
    }

    @Test
    void getCompanyByApiKeyThrowsNotFoundExceptionWhenNotExists() {
        UUID key = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> companyService.getCompanyByApiKey(key));
        assertEquals(COMPANY_APIKEY_NOT_FOUND, ex.getMessage());
    }

    @Test
    void createCompanySavesWhenApiKeyNotExists() {
        UUID key = UUID.randomUUID();
        Company toSave = Company.builder().companyName("X").companyApiKey(key).build();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.empty());
        when(companyRepository.save(any())).thenReturn(toSave);
        assertEquals(toSave, companyService.createCompany("X", key));
    }

    @Test
    void createCompanyThrowsWhenApiKeyAlreadyExists() {
        UUID key = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.of(mock(Company.class)));
        ApiKeyAlreadyExistsException ex = assertThrows(
                ApiKeyAlreadyExistsException.class,
                () -> companyService.createCompany("X", key)
        );
        assertEquals(API_KEY_ALREADY_EXISTS, ex.getMessage());
    }

    @Test
    void deleteCompanyByIdReturnsDeletedCompanyWhenExists() {
        Company c = Company.builder().companyId(1L).build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(c));
        Company deleted = companyService.deleteCompanyById(1L);
        assertEquals(c, deleted);
        verify(companyRepository).delete(c);
    }

    @Test
    void deleteCompanyByIdThrowsNotFoundExceptionWhenNotExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> companyService.deleteCompanyById(1L));
        assertEquals(COMPANY_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void updateCompanyUpdatesWhenApiKeyUnchanged() {
        UUID key = UUID.randomUUID();
        Company existing = Company.builder().companyId(1L).companyName("Old").companyApiKey(key).build();
        Company updated = Company.builder().companyId(1L).companyName("New").companyApiKey(key).build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.save(existing)).thenReturn(updated);
        assertEquals(updated, companyService.updateCompany(1L, "New", key));
    }

    @Test
    void updateCompanyUpdatesWhenApiKeyChangedAndNotExists() {
        UUID oldKey = UUID.randomUUID();
        UUID newKey = UUID.randomUUID();
        Company existing = Company.builder().companyId(1L).companyName("Old").companyApiKey(oldKey).build();
        Company updated = Company.builder().companyId(1L).companyName("New").companyApiKey(newKey).build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.findByCompanyApiKey(newKey)).thenReturn(Optional.empty());
        when(companyRepository.save(existing)).thenReturn(updated);
        assertEquals(updated, companyService.updateCompany(1L, "New", newKey));
    }

    @Test
    void updateCompanyThrowsWhenIdNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> companyService.updateCompany(1L, "N", UUID.randomUUID()));
        assertEquals(COMPANY_ID_NOT_FOUND, ex.getMessage());
    }

    @Test
    void updateCompanyThrowsWhenApiKeyAlreadyExists() {
        UUID oldKey = UUID.randomUUID();
        UUID newKey = UUID.randomUUID();
        Company existing = Company.builder().companyId(1L).companyApiKey(oldKey).build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.findByCompanyApiKey(newKey)).thenReturn(Optional.of(mock(Company.class)));
        ApiKeyAlreadyExistsException ex = assertThrows(
                ApiKeyAlreadyExistsException.class,
                () -> companyService.updateCompany(1L, "N", newKey)
        );
        assertEquals(API_KEY_ALREADY_EXISTS, ex.getMessage());
    }

    @Test
    void existsCompanyApiKeyReturnsTrueWhenExists() {
        UUID key = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.of(mock(Company.class)));
        assertTrue(companyService.existsCompanyApiKey(key));
    }

    @Test
    void existsCompanyApiKeyReturnsFalseWhenNotExists() {
        UUID key = UUID.randomUUID();
        when(companyRepository.findByCompanyApiKey(key)).thenReturn(Optional.empty());
        assertFalse(companyService.existsCompanyApiKey(key));
    }
}
