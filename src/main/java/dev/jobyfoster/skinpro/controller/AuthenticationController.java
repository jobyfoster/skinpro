package dev.jobyfoster.skinpro.controller;

import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.dto.SignupRequest;
import dev.jobyfoster.skinpro.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/signup")
    public String signUp(SignupRequest signupRequest, RedirectAttributes redirectAttributes) {
        try {
            authenticationService.signUp(signupRequest);
            redirectAttributes.addFlashAttribute("signupSuccess", "Registration successful. Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Signup failed: " + e.getMessage());
            return "redirect:/signup";
        }
    }

    @PostMapping("/signin")
    public String signIn(@ModelAttribute SigninRequest signinRequest, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationService.signIn(signinRequest, request, response);

            return "redirect:/dashboard";
        } catch (AuthenticationException e) {

            redirectAttributes.addFlashAttribute("error", "Authentication failed: Invalid username or password.");
            return "redirect:/login";
        }
    }


}
