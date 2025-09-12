package com.example.bankcards.config;

import com.example.bankcards.config.properties.SecurityProperties;
import com.example.bankcards.security.AccessDenied;
import com.example.bankcards.security.JsonAuthenticationEntryPoint;
import com.example.bankcards.security.JwtAuthFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WebSecurityConfig {

    JwtAuthFilter jwtAuthFilter;
    UserDetailsService userDetailsService;
    SecurityProperties securityProperties;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(securityProperties.getAllowedSources());
                    corsConfig.setAllowedMethods(securityProperties.getAllowedMethods());
                    corsConfig.setAllowedHeaders(securityProperties.getAllowedHeaders());
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(securityProperties.getIgnoredUrls().toArray(new String[0])).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .anyRequest().authenticated()
                                      )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        log.info(">>> Ignored URLs: {}", securityProperties.getIgnoredUrls());
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDenied();
    }



    @Bean
    public AuthenticationEntryPoint entryPoint() {
        return new JsonAuthenticationEntryPoint();
    }

}
