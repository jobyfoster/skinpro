package dev.jobyfoster.skinpro.service;

import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.dto.SignupRequest;
import dev.jobyfoster.skinpro.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    User signUp(SignupRequest signupRequest);

    void signIn(SigninRequest signinRequest, HttpServletRequest request, HttpServletResponse response);

}
