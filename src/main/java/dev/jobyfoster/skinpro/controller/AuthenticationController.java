package dev.jobyfoster.skinpro.controller;

import dev.jobyfoster.skinpro.dto.SigninRequest;
import dev.jobyfoster.skinpro.dto.SignupRequest;
import dev.jobyfoster.skinpro.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


// Marks this class as a Spring MVC controller handling requests for '/api/v1/auth' path
@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    // Dependency injection of AuthenticationService to handle the business logic of authentication
    private final AuthenticationService authenticationService;
    /**
     * Handles the signup process.
     *
     * @param signupRequest The request containing user signup information.
     * @param redirectAttributes Attributes for a redirect scenario.
     * @return A redirect URL to the login page on successful signup or back to the signup page with an error.
     */
    @PostMapping("/signup")
    public String signUp(SignupRequest signupRequest, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to sign up the user with the provided request data
            authenticationService.signUp(signupRequest);
            // On success, add a success message and redirect to the login page
            redirectAttributes.addFlashAttribute("signupSuccess", "Registration successful. Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            // On failure, add an error message and redirect back to the signup page
            redirectAttributes.addFlashAttribute("error", "Signup failed: " + e.getMessage());
            return "redirect:/signup";
        }
    }

    /**
     * Handles the signin process.
     *
     * @param signinRequest The request containing user signin information.
     * @param redirectAttributes Attributes for a redirect scenario.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return A redirect URL to the dashboard on successful signin or back to the login page with an error.
     */
    @PostMapping("/signin")
    public String signIn(@ModelAttribute SigninRequest signinRequest, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Attempt to sign in the user with the provided credentials
            authenticationService.signIn(signinRequest, request, response);
            // On success, redirect to the user dashboard
            return "redirect:/dashboard";
        } catch (AuthenticationException e) {
            // On failure, add an error message and redirect back to the login page
            redirectAttributes.addFlashAttribute("error", "Authentication failed: Invalid username or password.");
            return "redirect:/login";
        }
    }
}

