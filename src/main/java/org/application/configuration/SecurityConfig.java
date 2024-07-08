package org.application.configuration;

import org.application.constant.AuthorityConstants;
import org.application.constant.PathConstants;
import org.application.security.CustomUserDetailService;
import org.application.security.JwtAuthEntryPoint;
import org.application.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private JwtAuthEntryPoint authEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(JwtAuthEntryPoint authEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailService customUserDetailService) {
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailService = customUserDetailService;
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
       return http.csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(req->req
                               .requestMatchers(PathConstants.REGISTER_ROUTE, PathConstants.LOGIN_ROUTE).permitAll()
                               .requestMatchers(PathConstants.REGISTER_ROUTE, PathConstants.UPLOAD_ROUTE).hasAuthority(AuthorityConstants.ADMIN)
                               .requestMatchers(PathConstants.REGISTER_ROUTE, PathConstants.DOWNLOAD_ROUTE).hasAuthority(AuthorityConstants.ADMIN)
                               .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                               .requestMatchers(PathConstants.PRUEBA_ROUTE).hasAuthority(AuthorityConstants.ADMIN)
                               .anyRequest()
                               .authenticated())
               .exceptionHandling(exception -> exception
                       .authenticationEntryPoint(authEntryPoint))
               .sessionManagement(session->session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .userDetailsService(customUserDetailService)
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
