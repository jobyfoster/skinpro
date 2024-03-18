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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository repo = new HttpSessionSecurityContextRepository();

    private final UserService userService;

    public User signUp(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken.");
        }

        User user = new User();
        user.setPoints(0);
        user.setLastLogin(new Date(System.currentTimeMillis()));
        user.setStreak(0);
        user.setEnabled(true);
        user.setEmail(signupRequest.getEmail());
        user.setRole(Role.USER);
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordConfig.passwordEncoder().encode(signupRequest.getPassword()));

        return userRepository.save(user);
    }

    public void signIn(SigninRequest signinRequest, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getUsername(),
                        signinRequest.getPassword()
                )
        );

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        repo.saveContext(context, request, response);
    }

}

