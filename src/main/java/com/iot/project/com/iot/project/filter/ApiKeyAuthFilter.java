package com.iot.project.com.iot.project.filter;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.NotFoundException; // o tu excepción personalizada
import com.iot.project.com.iot.project.service.CompanyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final CompanyService companyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        List<String> securedPaths = List.of("/api/locations", "/api/sensors");
        boolean shouldFilter = securedPaths.stream()
                .anyMatch(requestUri::startsWith);
        if (!shouldFilter) {
            filterChain.doFilter(request, response);
            return;
        }
        String apiKeyHeader = request.getHeader("X-API-KEY");

        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Missing API Key");
            return;
        }

        UUID apiKey;
        try {
            apiKey = UUID.fromString(apiKeyHeader);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Invalid API Key format");
            return;
        }

        try {
            Company company = companyService.getCompanyByApiKey(apiKey);
            request.setAttribute("authenticatedCompany", company);
            request.setAttribute("authenticatedCompanyId", company.getCompanyId());
            filterChain.doFilter(request, response);
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Invalid API Key");
        }
    }
}
