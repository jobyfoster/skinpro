package dev.jobyfoster.skinpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("user", userDetails);
        return "dashboard";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get existing session without creating a new one
        if (session != null) {
            System.out.println("Login Session ID: " + session.getId());
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername() + " is logging out.");

        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}

