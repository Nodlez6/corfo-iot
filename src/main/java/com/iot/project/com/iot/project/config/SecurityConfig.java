package com.iot.project.com.iot.project.config;

import com.iot.project.com.iot.project.filter.ApiKeyAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.iot.project.com.iot.project.filter.CustomAuthenticationEntryPoint;
import com.iot.project.com.iot.project.security.AdminDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Bean
    public ApiKeyAuthFilter apiKeyAuthFilter() {
        return new ApiKeyAuthFilter(exceptionResolver);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(adminDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .addFilterBefore(apiKeyAuthFilter(), LogoutFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults()) // Autenticación básica
                .authenticationProvider(authenticationProvider()) // <- Usa el nuevo proveedor basado en BD
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                        .permitAll()
                // .requestMatchers("/api/v1/company").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.GET, "/api/v1/company").permitAll()
                // .requestMatchers(HttpMethod.POST, "/api/v1/company").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.PUT, "/api/v1/company").hasRole("ADMIN")
                // .requestMatchers(HttpMethod.DELETE, "/api/v1/company").hasRole("ADMIN")
                // .anyRequest().denyAll()
                )
                .addFilterBefore(apiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// @Autowired
// private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

// @Autowired
// @Qualifier("handlerExceptionResolver")
// private HandlerExceptionResolver exceptionResolver;

// @Bean
// public ApiKeyAuthFilter apiKeyAuthFilter(){
// return new ApiKeyAuthFilter(exceptionResolver);
// }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// // .addFilterBefore(apiKeyAuthFilter(), LogoutFilter.class)
// .csrf(AbstractHttpConfigurer::disable)
// .httpBasic(Customizer.withDefaults()) // Autenticación básica
// .exceptionHandling(exception ->
// exception.authenticationEntryPoint(customAuthenticationEntryPoint)
// )
// .authorizeHttpRequests(authz -> authz
// .requestMatchers("/api/v1/**").permitAll()
// .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
// "/swagger-resources/**", "/webjars/**").permitAll()
// // .requestMatchers("/api/v1/company").hasRole("ADMIN")
// // .requestMatchers(HttpMethod.GET, "/api/v1/company").permitAll()
// // .requestMatchers(HttpMethod.POST, "/api/v1/company").hasRole("ADMIN")
// // .requestMatchers(HttpMethod.PUT, "/api/v1/company").hasRole("ADMIN")
// // .requestMatchers(HttpMethod.DELETE, "/api/v1/company").hasRole("ADMIN")
// .anyRequest().denyAll()
// )
// .addFilterBefore(apiKeyAuthFilter(),
// UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public AuthenticationManager authenticationManager(HttpSecurity http) throws
// Exception {
// return http.getSharedObject(AuthenticationManagerBuilder.class)
// .userDetailsService(userDetailsService())
// .passwordEncoder(passwordEncoder())
// .and()
// .build();
// }

// @Bean
// public AuthenticationProvider authenticationProvider() {
// DaoAuthenticationProvider authenticationProvider = new
// DaoAuthenticationProvider();

// // Usar un encoder más seguro
// authenticationProvider.setPasswordEncoder(passwordEncoder());
// authenticationProvider.setUserDetailsService(userDetailsService());

// return authenticationProvider;
// }

// @Bean
// public UserDetailsService userDetailsService() {
// UserDetails userAdmin = User.builder()
// .username("admin")
// .password(passwordEncoder().encode("12345")) // Contraseña codificada
// .roles("ADMIN") // Roles deben ser asignados con el prefijo "ROLE_"
// .build();

// UserDetails user = User.builder()
// .username("user")
// .password(passwordEncoder().encode("1234")) // Contraseña codificada
// .roles("USER") // Roles deben ser asignados con el prefijo "ROLE_"
// .build();

// return new InMemoryUserDetailsManager(userAdmin, user);
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder(); // Usamos BCryptPasswordEncoder para
// mayor seguridad
// }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// // .addFilterBefore(apiKeyAuthFilter(), LogoutFilter.class)
// .csrf(AbstractHttpConfigurer::disable)
// .httpBasic(Customizer.withDefaults()) // Autenticación básica
// .exceptionHandling(exception ->
// exception.authenticationEntryPoint(customAuthenticationEntryPoint)
// )
// .authorizeHttpRequests(authz -> {
// authz.requestMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN");
// authz.requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN");
// authz.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN");
// authz.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
// authz.anyRequest().denyAll();
// })
// .addFilterBefore(apiKeyAuthFilter(),
// UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// .csrf(AbstractHttpConfigurer::disable)
// .httpBasic(Customizer.withDefaults()) // Autenticación básica
// .exceptionHandling(exception ->
// exception.authenticationEntryPoint(customAuthenticationEntryPoint))
// .authorizeHttpRequests(authz -> authz
// .requestMatchers("/api/v1/sensor/**").permitAll() // Permite todas las rutas
// de sensor
// .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
// "/swagger-resources/**", "/webjars/**").permitAll()
// .anyRequest().denyAll()
// )
// .addFilterBefore(apiKeyAuthFilter(),
// UsernamePasswordAuthenticationFilter.class); // Asegúrate de que tu filtro
// esté antes

// return http.build();
// }

// private final ApiKeyAuthFilter apiKeyAuthFilter;

// public SecurityConfig(ApiKeyAuthFilter apiKeyAuthFilter) {
// this.apiKeyAuthFilter = apiKeyAuthFilter;
// }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
// http
// .securityMatcher("/afsdfadsafsd/**") // Solo se aplica a estas rutas
// .csrf(AbstractHttpConfigurer::disable)
// .authorizeHttpRequests(authz -> authz
// .anyRequest().authenticated()
// )
// .addFilterBefore(apiKeyAuthFilter,
// UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
// http
// .securityMatcher("/**")
// .csrf(AbstractHttpConfigurer::disable)
// .authorizeHttpRequests(auth -> auth
// .anyRequest().permitAll()
// );
// return http.build();
// }
// }
