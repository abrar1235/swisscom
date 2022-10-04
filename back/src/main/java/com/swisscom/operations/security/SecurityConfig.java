package com.swisscom.operations.security;

import com.swisscom.operations.util.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] SECURE_LINKS = {"/api/v1/maintenance/updateMaintenance", "/api/v1/maintenance/deleteMaintenance",
            "/api/v1/maintenance/addMaintenance", "/api/v1/user/addUser",
            "/api/v1/user/updateUser", "/api/v1/user/deleteUser"};
    private final AppUtil appUtil;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(SECURE_LINKS).authenticated().anyRequest().permitAll()
                .and().csrf().disable()
                .cors().and()
                .httpBasic().and()
                .addFilterBefore(new JWTAuthenticationFilter(appUtil, SECURE_LINKS), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().frameOptions().disable();
        return http.build();
    }
}