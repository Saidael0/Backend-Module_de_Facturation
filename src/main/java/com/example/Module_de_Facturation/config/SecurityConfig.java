package com.example.Module_de_Facturation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration de la sécurité de l'application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure les règles de sécurité et les autorisations
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF pour simplifier les tests (à activer en production)
                .csrf(csrf -> csrf.disable())

                // Configure les autorisations :
                .authorizeHttpRequests(auth -> auth
                        // Autorise l'accès à Swagger sans authentification
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                // Active l'authentification Basic HTTP
                .httpBasic(withDefaults());

        return http.build();
    }

    /**
     * Crée des utilisateurs en mémoire pour la démo
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // Création de l'utilisateur admin
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123")) // Mot de passe crypté
                .roles("ADMIN")
                .build();

        // Création de l'utilisateur standard
        UserDetails user = User.builder()
                .username("saida")
                .password(passwordEncoder.encode("saida123"))
                .roles("USER")
                .build();

        // Retourne le gestionnaire avec les deux utilisateurs
        return new InMemoryUserDetailsManager(admin, user);
    }

    /**
     * Configure l'encodeur de mots de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}