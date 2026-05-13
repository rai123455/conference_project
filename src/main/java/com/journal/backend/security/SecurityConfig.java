package com.journal.backend.security;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ← добавь
                .csrf(csrf -> csrf.disable())
                // ... остальное без изменений
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Открываем регистрацию и логин полностью
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/articles/published").permitAll()
                        .requestMatchers("/api/articles/*/file").permitAll()    // ← добавь
                        .requestMatchers("/api/articles/upload").authenticated() // ← добавь
                        .requestMatchers("/api/articles/author/**").authenticated()

                        .requestMatchers("/api/chair/**").hasRole("CHAIR") // для председателя

                        // Защищённые эндпоинты по ролям
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/articles/submit-review").hasRole("REVIEWER")

                        // Всё остальное требует авторизации
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt — алгоритм хеширования паролей
    // Пароль "12345" → "$2a$10$xK8..." — обратно не расшифровать
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500"));
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "https://stellar-smakager-f7e9fd.netlify.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE",  "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        return new UrlBasedCorsConfigurationSource(){{
            registerCorsConfiguration("/**", config);
        }};
    }
}