package com.iot.project.com.iot.project.controller;

import com.iot.project.com.iot.project.config.AppProperties;
import com.iot.project.com.iot.project.dto.company.CompanyResponseDto;
import com.iot.project.com.iot.project.dto.company.CreateCompanyRequest;
import com.iot.project.com.iot.project.dto.company.UpdateCompanyRequest;
import com.iot.project.com.iot.project.dto.wrapper.ActionMethod;
import com.iot.project.com.iot.project.dto.wrapper.ServiceResponse;
import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private CompanyController controller;

    private static final String APP_NAME = "iotService";

    @BeforeEach
    void setUp() {
        when(appProperties.getResponseKey()).thenReturn(APP_NAME);
        controller.init();
    }

    @Test
    void getAllCompaniesShouldReturnListWrapped() {
        Company c1 = Company.builder().companyId(1L).companyName("A").companyApiKey(UUID.randomUUID()).build();
        Company c2 = Company.builder().companyId(2L).companyName("B").companyApiKey(UUID.randomUUID()).build();
        when(companyService.getCompanies()).thenReturn(List.of(c1, c2));

        ResponseEntity<ServiceResponse<List<CompanyResponseDto>>> response = controller.getAllCompanies();
        ServiceResponse<List<CompanyResponseDto>> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, List<CompanyResponseDto>>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, List<CompanyResponseDto>> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANIES.name()));
        List<CompanyResponseDto> dataList = innerMap.get(ActionMethod.COMPANIES.name());
        assertEquals(2, dataList.size());
        assertEquals(c1.getCompanyName(), dataList.get(0).getCompanyName());
    }

    @Test
    void getCompanyByIdShouldReturnWrappedCompany() {
        Company c = Company.builder().companyId(3L).companyName("X").companyApiKey(UUID.randomUUID()).build();
        when(companyService.getCompanyById(3L)).thenReturn(c);

        ResponseEntity<ServiceResponse<CompanyResponseDto>> response = controller.getCompanyById(3L);
        ServiceResponse<CompanyResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, CompanyResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, CompanyResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANY.name()));
        CompanyResponseDto dto = innerMap.get(ActionMethod.COMPANY.name());
        assertEquals(c.getCompanyId(), dto.getCompanyId());
    }

    @Test
    void getCompanyByApiKeyShouldReturnWrappedCompany() {
        UUID key = UUID.randomUUID();
        Company c = Company.builder().companyId(4L).companyApiKey(key).companyName("Z").build();
        when(companyService.getCompanyByApiKey(key)).thenReturn(c);

        ResponseEntity<ServiceResponse<CompanyResponseDto>> response = controller.getCompanyByApiKey(key);
        ServiceResponse<CompanyResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, CompanyResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, CompanyResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANY.name()));
        CompanyResponseDto dto = innerMap.get(ActionMethod.COMPANY.name());
        assertEquals(key, dto.getCompanyApiKey());
    }

    @Test
    void createCompanyShouldReturnCreatedStatus() {
        UUID key = UUID.randomUUID();
        CreateCompanyRequest req = CreateCompanyRequest.builder().companyName("NewCo").apiKey(key).build();
        Company created = Company.builder().companyId(5L).companyName("NewCo").companyApiKey(key).build();
        when(companyService.createCompany("NewCo", key)).thenReturn(created);

        ResponseEntity<ServiceResponse<CompanyResponseDto>> response = controller.createCompany(req);
        ServiceResponse<CompanyResponseDto> body = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, CompanyResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, CompanyResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANY.name()));
        CompanyResponseDto dto = innerMap.get(ActionMethod.COMPANY.name());
        assertEquals(created.getCompanyId(), dto.getCompanyId());
    }

    @Test
    void updateCompanyShouldReturnCreatedStatus() {
        UUID key = UUID.randomUUID();
        UpdateCompanyRequest req = UpdateCompanyRequest.builder().companyName("UpdCo").apiKey(key).build();
        Company updated = Company.builder().companyId(6L).companyName("UpdCo").companyApiKey(key).build();
        when(companyService.updateCompany(6L, "UpdCo", key)).thenReturn(updated);

        ResponseEntity<ServiceResponse<CompanyResponseDto>> response = controller.updateCompany(6L, req);
        ServiceResponse<CompanyResponseDto> body = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, CompanyResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, CompanyResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANY.name()));
        CompanyResponseDto dto = innerMap.get(ActionMethod.COMPANY.name());
        assertEquals(updated.getCompanyName(), dto.getCompanyName());
    }

    @Test
    void deleteCompanyShouldReturnOkStatus() {
        Company deleted = Company.builder().companyId(7L).companyName("DelCo").companyApiKey(UUID.randomUUID()).build();
        when(companyService.deleteCompanyById(7L)).thenReturn(deleted);

        ResponseEntity<ServiceResponse<CompanyResponseDto>> response = controller.deleteCompany(7L);
        ServiceResponse<CompanyResponseDto> body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        Map<String, Map<String, CompanyResponseDto>> respMap = body.getResponse();
        assertTrue(respMap.containsKey(APP_NAME));
        Map<String, CompanyResponseDto> innerMap = respMap.get(APP_NAME);
        assertTrue(innerMap.containsKey(ActionMethod.COMPANY.name()));
        CompanyResponseDto dto = innerMap.get(ActionMethod.COMPANY.name());
        assertEquals(deleted.getCompanyId(), dto.getCompanyId());
    }
}
