package com.iot.project.com.iot.project.filter;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.UnauthorizedException;
import com.iot.project.com.iot.project.service.CompanyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final CompanyService companyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new UnauthorizedException("Unauthorized: Missing API Key");
        }

        Company company = companyService.getCompanyByApiKey(apiKey);
        if (company == null) {
            throw new UnauthorizedException("Unauthorized: Invalid company-api-key");
        }

        // Con esto se me ocurre cuando alguna accion necesitemos el company que validamos, pasarlo desde el controaldor
        request.setAttribute("authenticatedCompany", company);

        filterChain.doFilter(request, response);
    }
}
