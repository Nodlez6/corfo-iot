package com.iot.project.com.iot.project.filter;

import java.util.List;
import java.util.UUID;
import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.exception.UnauthorizedException;
import com.iot.project.com.iot.project.service.CompanyService;


public class ApiKeyAuthFilter extends OncePerRequestFilter {

 private final HandlerExceptionResolver exceptionResolver;

    @Autowired
    private CompanyService companyService;

    public ApiKeyAuthFilter(HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

                try {
                    String method = request.getMethod();
                    String requestUri = request.getRequestURI();

                    System.out.println("Intercepting request: " + requestUri + " | Method: " + method);  // Agrega esto para depurar


                    List<String> protectedPaths = List.of(
                        "/api/v1/company"
                        // "/api/v1/sensor"
                        //  "/api/v1/location"
                    );
        
                    // 2. Permitir explícitamente acceso a esta ruta pública (sin API Key)
                    // if (requestUri.matches("/api/v1/sensor/apiKey/.*") && method.equalsIgnoreCase("GET")) {
                    //     System.out.println("MENU - 2");
                    //     filterChain.doFilter(request, response);
                    //     return;
                    // }


                    // Permitir acceso sin autenticación a Swagger y documentación
                    if (requestUri.startsWith("/api/v1/admin")) {
                        filterChain.doFilter(request, response);
                        return;
                    }



                    // Permitir acceso sin autenticación a Swagger y documentación
                    if (requestUri.startsWith("/swagger-ui") || 
                        requestUri.startsWith("/v3/api-docs") || 
                        requestUri.startsWith("/swagger-resources") ||
                        requestUri.startsWith("/webjars")) {
                        filterChain.doFilter(request, response);
                        return;
                    }


                    boolean shouldFilter = protectedPaths.stream().anyMatch(requestUri::startsWith);
        
                    if (shouldFilter) {
                        System.out.println("MENU - 1");
                        String authorizationHeader = request.getHeader("Authorization");
                        if (authorizationHeader == null || authorizationHeader.isBlank()) {
                            throw new UnauthorizedException("Header 'Authorization' is required.");
                        }
                        filterChain.doFilter(request, response);
                        return;
                    }
        
                    System.out.println("MENU - 5");
        
        
        
                    // if (requestUri.startsWith("/api/sensorData") && method.equalsIgnoreCase("POST")) {
                    //     filterChain.doFilter(request, response);
                    //     return;
                    // }
        
 
                    String apiKeyHeader = request.getHeader("X-API-KEY");
                    if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
                        throw new UnauthorizedException("Missing API Key (X-API-KEY)");
                    }
                    

                    UUID apiKey;
                    try {
                        apiKey = UUID.fromString(apiKeyHeader);
                    } catch (IllegalArgumentException e) {
                        throw new UnauthorizedException("Invalid API Key format");
                    }
                    

                    Company company = companyService.getCompanyByApiKey(apiKey);
                    request.setAttribute("authenticatedCompany", company);
                    request.setAttribute("authenticatedCompanyId", company.getCompanyId());
                    
                    System.out.println("MENU - 8");

                    filterChain.doFilter(request, response);
                    System.out.println("MENU - 9");
        
                } catch (Exception ex) {
                    exceptionResolver.resolveException(request, response, null, ex);
                }
            }
        }
        

        



    //     try {
    //         String method = request.getMethod();
    //         String requestUri = request.getRequestURI();

    //         System.out.println("Intercepting request: " + requestUri + " | Method: " + method);  // Agrega esto para depurar


    //         List<String> protectedPaths = List.of(
    //             "/api/v1/company"
    //             // "/api/v1/sensor"
    //             //  "/api/v1/location"
    //         );

    //         // 2. Permitir explícitamente acceso a esta ruta pública (sin API Key)
    //         // if (requestUri.matches("/api/v1/sensor/apiKey/.*") && method.equalsIgnoreCase("GET")) {
    //         //     System.out.println("MENU - 2");
    //         //     filterChain.doFilter(request, response);
    //         //     return;
    //         // }


            

    //         // Permitir acceso sin autenticación a Swagger y documentación
    //         if (requestUri.startsWith("/swagger-ui") || 
    //             requestUri.startsWith("/v3/api-docs") || 
    //             requestUri.startsWith("/swagger-resources") ||
    //             requestUri.startsWith("/webjars")) {
    //             filterChain.doFilter(request, response);
    //             return;
    //         }


    //         boolean shouldFilter = protectedPaths.stream().anyMatch(requestUri::startsWith);

    //         if (shouldFilter) {
    //             System.out.println("MENU - 1");
    //             String authorizationHeader = request.getHeader("Authorization");
    //             if (authorizationHeader == null || authorizationHeader.isBlank()) {
    //                 throw new UnauthorizedException("Header 'Authorization' is required.");
    //             }
    //             filterChain.doFilter(request, response);
    //             return;
    //         }

    //         System.out.println("MENU - 5");



    //         // if (requestUri.startsWith("/api/sensorData") && method.equalsIgnoreCase("POST")) {
    //         //     filterChain.doFilter(request, response);
    //         //     return;
    //         // }


    //         String apiKeyHeader = request.getHeader("X-API-KEY");
    //         if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
    //             throw new UnauthorizedException("Missing API Key (X-API-KEY)");
    //         }
            
    //         System.out.println("MENU - 6");

    //         UUID apiKey;
    //         try {
    //             apiKey = UUID.fromString(apiKeyHeader);
    //         } catch (IllegalArgumentException e) {
    //             throw new UnauthorizedException("Invalid API Key format");
    //         }
            
    //         System.out.println("MENU - 7");

    //         Company company = companyService.getCompanyByApiKey(apiKey);
    //         request.setAttribute("authenticatedCompany", company);
    //         request.setAttribute("authenticatedCompanyId", company.getCompanyId());
            
    //         System.out.println("MENU - 8");

    //         filterChain.doFilter(request, response);
    //         System.out.println("MENU - 9");

    //     } catch (Exception ex) {
    //         exceptionResolver.resolveException(request, response, null, ex);
    //     }
    // }
