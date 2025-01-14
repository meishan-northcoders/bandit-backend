package com.northcoders.bandit.config;

import com.northcoders.bandit.filter.FirebaseAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Bean
//    public FirebaseAuthFilter firebaseAuthFilter() {
//        return new FirebaseAuthFilter();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/public/**",
                                "/api/v1/**"
                        )
                        .permitAll() // replace with wildcard to permit
                        .anyRequest().authenticated()
                )
//                .addFilterBefore(firebaseAuthFilter(),UsernamePasswordAuthenticationFilter.class)
                ;
        return http.build();
    }
}