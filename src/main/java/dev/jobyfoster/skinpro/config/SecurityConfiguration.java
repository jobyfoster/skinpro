package dev.jobyfoster.skinpro.config;

import dev.jobyfoster.skinpro.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final PasswordConfig passwordConfig;
    // Instantiates a default SecurityContextRepository for HTTP sessions
    private final SecurityContextRepository repo = new HttpSessionSecurityContextRepository();

    // Bean definition for session management, enabling session tracking
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // Configures the global AuthenticationManager with custom user details and password encoding
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    // Bean definition for configuring security filters and rules for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configures authorization rules for various endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Allows public access to specified endpoints
                        .requestMatchers("/api/v1/auth/**", "/signup", "/login", "/", "/styles.css").permitAll()
                        // Restricts access to admin endpoints to users with ROLE_ADMIN authority
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                        // Restricts access to user endpoints to users with ROLE_USER authority
                        .requestMatchers("/api/v1/user/**", "/dashboard", "/dashboard/**", "/survey", "/survey/**", "/logout").hasAuthority("ROLE_USER")
                        // Allows public access to the error page
                        .requestMatchers("/error").permitAll()
                        // Requires authentication for all other requests
                        .anyRequest().authenticated())
                // Configures form-based login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .permitAll())
                // Configures logout behavior
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        // Redirects to login page after logout
                        .logoutSuccessUrl("/login?logout"))
                // Configures session management policies
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1).maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry()))
                // Configures a custom SecurityContextRepository
                .securityContext((context) -> context
                        .securityContextRepository(repo)
                );

        return http.build();
    }

    // Bean definition for custom authentication provider with user details service and password encoder
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordConfig.passwordEncoder());
        return authenticationProvider;
    }

    // Bean definition for creating the AuthenticationManager using the ApplicationContext
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, ApplicationContext applicationContext) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}


