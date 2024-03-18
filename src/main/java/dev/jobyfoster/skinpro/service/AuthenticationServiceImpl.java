package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.config.PasswordConfig;
import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.dto.SignupRequest;
import dev.jobyfoster.skinpro.model.Role;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    // Dependencies are automatically injected by Spring's Inversion of Control container.
    // This service depends on a UserRepository for database operations,
    // PasswordConfig for password encoding, AuthenticationManager for authentication processes,
    // and a SecurityContextHolderStrategy and SecurityContextRepository for security context management.
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository repo = new HttpSessionSecurityContextRepository();

    // signUp method takes a SignupRequest, checks if the username exists,
    // then creates and saves a new User with initialized properties.
    public User signUp(SignupRequest signupRequest) {
        // Checks if the requested username is already taken.
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken.");
        }

        // Creates a new User object and sets its properties based on the signup request and defaults.
        User user = new User();
        user.setPoints(0); // Initializes points to 0 for a new user.
        user.setLastLogin(new Date(System.currentTimeMillis())); // Sets the last login to the current time.
        user.setStreak(0); // Initializes streak to 0 for a new user.
        user.setEnabled(true); // Enables the new user account by default.
        user.setEmail(signupRequest.getEmail()); // Sets email from the signup request.
        user.setRole(Role.USER); // Assigns a default role of USER.
        user.setUsername(signupRequest.getUsername()); // Sets username from the signup request.
        user.setPassword(passwordConfig.passwordEncoder().encode(signupRequest.getPassword())); // Encodes and sets the user's password.

        // Saves the new user to the repository and returns it.
        return userRepository.save(user);
    }

    // signIn method attempts to authenticate a user with the provided signinRequest credentials.
    public void signIn(SigninRequest signinRequest, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Authenticates the user with the provided username and password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getUsername(),
                        signinRequest.getPassword()
                )
        );

        // Creates a new, empty SecurityContext, sets the authentication, and stores it in the SecurityContextHolderStrategy.
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        // Saves the SecurityContext in the repository, typically a session store.
        repo.saveContext(context, request, response);
    }
}


