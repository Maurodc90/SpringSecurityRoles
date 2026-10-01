package com.maurodelcore.employee_management.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring Security configuration for the employee management app.
 * Defines the hard-coded users, the password hashing, and which
 * roles may access which endpoints.
 */
@Configuration
public class EmployeeSecurity {

    /**
     * Provides the BCrypt encoder used to hash passwords before they are
     * stored and to check the password typed at login against the stored hash.
     *
     * @return the password encoder shared by the whole application
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the two hard-coded users for this exercise, kept in memory.
     * A real application would load users from a database instead.
     * Both passwords are hashed with BCrypt, never stored as plain text.
     *
     * @return the user store containing the admin and the employee
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails mauro = User.withUsername("Mauro")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        UserDetails elcin = User.withUsername("Elcin")
                .password(passwordEncoder().encode("1234"))
                .roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(mauro, elcin);
    }

    /**
     * Defines the security rules applied to every incoming request:
     * which URLs are public, which need a specific role, how users
     * log in, and how they log out.
     *
     * @param http builder provided by Spring for configuring web security
     * @return the finished chain of security filters
     * @throws Exception if the configuration cannot be built
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF protection is OFF for this practice project only.
                // With it on, state-changing requests (POST, PUT, DELETE, and the
                // /logout POST) are rejected with 403 unless they carry a CSRF token,
                // which makes testing from Postman awkward. This app has no real data.
                // In an app with cookie-based login and real users, leave it ON.
                .csrf(csrf -> csrf.disable())

                // Authorization rules: checked top to bottom, first match wins
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/home").permitAll()                    // public, no login needed
                        .requestMatchers("/admin/**").hasRole("ADMIN")           // ADMIN only
                        .requestMatchers("/employees/**").hasRole("EMPLOYEE")    // EMPLOYEE only
                        .anyRequest().authenticated())                           // everything else: any logged-in user

                // Browser login through Spring's generated login page
                .formLogin(withDefaults())

                // Header-based login, used by tools like Postman and curl
                .httpBasic(withDefaults())

                // Logout: end the session, remove the cookie, reply with a plain message
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.getWriter().write("Logout success");
                        })
                );

        return http.build();
    }
}