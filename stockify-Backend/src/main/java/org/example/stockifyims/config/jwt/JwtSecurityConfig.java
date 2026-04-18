package org.example.stockifyims.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/comments/**").permitAll()
                        // Public auth routes
                        .requestMatchers("/auth/login", "/auth/register", "/auth/forgot-password", "/auth/verify-otp", "/auth/reset-password", "/auth/test").permitAll()
                        // Static & view resources
                        .requestMatchers("/js/**", "/css/**", "/static/**", "/WEB-INF/view/**").permitAll()

                        // Public JSP pages + read-only product/report endpoints (JSP clients don't send JWT)
                        .requestMatchers(HttpMethod.GET,
                                "/",
                                "/login",
                                "/product",
                                "/product/",
                                "/product/all",
                                "/product/id/**",
                                "/product/download",
                                "/product/download/csv",
                                "/product/barcode/**",
                                "/report/barcode/**"
                        ).permitAll()

                        // ADMIN-only: product mutations, warehouse writes
                        .requestMatchers(HttpMethod.POST, "/product/save").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/product/delete/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/product/upload").hasRole("ADMIN")
                        // Warehouse reads: any logged-in user (purchase/sales forms need the list)
                        .requestMatchers(HttpMethod.GET, "/warehouse/all", "/warehouse/stock-overview", "/warehouse/movements").authenticated()
                        .requestMatchers(HttpMethod.POST, "/warehouse/save", "/warehouse/delete/**").hasRole("ADMIN")

                        // Both roles: create purchases/sales, other reports, dashboard
                        .requestMatchers("/purchase/**").authenticated()
                        .requestMatchers("/sales/**").authenticated()
                        .requestMatchers("/dashboard/**").authenticated()
                        // Sales/purchase PDFs: any logged-in user (not admin-only)
                        .requestMatchers(HttpMethod.GET, "/report/sales/**", "/report/purchase/**").authenticated()
                        .requestMatchers("/report/**").hasRole("ADMIN")
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/comment/**", "/comments/**").authenticated()

                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("401 Unauthorized: " + authException.getMessage() + " for "
                                    + request.getRequestURI());
                            response.sendError(401, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("403 Forbidden: " + accessDeniedException.getMessage() + " for "
                                    + request.getRequestURI());
                            response.sendError(403, "Forbidden");
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
