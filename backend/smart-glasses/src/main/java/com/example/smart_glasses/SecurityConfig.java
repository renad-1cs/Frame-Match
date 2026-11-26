package com.example.smart_glasses;

import com.example.smart_glasses.service.AdminDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            AdminDetailsService adminDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(adminDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {

        http
            .authenticationProvider(authenticationProvider)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.GET, "/api/frames/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/faceshapes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/matches/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/face-analysis/recommend/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/face-analysis").permitAll()

                .requestMatchers("/api/admins/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/frames/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/frames/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/faceshapes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/faceshapes/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/matches/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/matches/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/face-analysis/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {});

        return http.build();
    }
}