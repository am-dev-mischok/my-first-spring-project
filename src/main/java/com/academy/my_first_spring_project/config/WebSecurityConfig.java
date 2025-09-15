package com.academy.my_first_spring_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * WebSecurityConfig class configures web security for the application. It defines the security filter chain,
 * user details service, and password encoder.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Configures the security filter chain for the Spring Security framework.
     * This setup includes request authorization, form login, basic HTTP authentication,
     * session invalidation on logout, CSRF protection adjustments, and frame options headers.
     *
     * @param http HttpSecurity object to configure the web based security.
     * @return Configured SecurityFilterChain instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .requestMatchers("/").permitAll()
                .requestMatchers("/greeting/**").permitAll()
                .requestMatchers("/person").permitAll()
                .requestMatchers("/recommendation/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll() //remove before going live / pushing to remote
                .anyRequest().authenticated()));

        http.formLogin(Customizer.withDefaults());

        http.httpBasic(Customizer.withDefaults());

        http.logout(configurer -> configurer.invalidateHttpSession(true));

        //Remove / disable before live deployment / pushing to remote
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/person"));
        http.headers(headers -> headers.frameOptions().sameOrigin());


        return http.build();
    }

    /**
     * Provides a password encoder bean that uses the BCrypt hashing algorithm.
     * This encoder is used to hash passwords for security purposes, and it can be
     * integrated into authentication mechanisms in a Spring Security context.
     *
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
