package com.instagram.clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Desabilitar CSRF para APIs geralmente é seguro
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/users/register").permitAll()  // Permitir todos os acessos ao endpoint de registro
                .anyRequest().authenticated()  // Requerer autenticação para todas as outras requisições
            )
            .httpBasic();  // Configuração básica de autenticação HTTP, ajuste conforme necessário
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
