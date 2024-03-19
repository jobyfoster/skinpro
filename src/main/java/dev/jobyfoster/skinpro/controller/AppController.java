package dev.jobyfoster.skinpro.controller;

import com.azure.core.annotation.Get;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.SurveyResponse;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import dev.jobyfoster.skinpro.service.OpenAIService;
import dev.jobyfoster.skinpro.service.SkincareService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AppController {
    private final SkincareService skincareService;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;

    @Autowired
    public AppController(SkincareService skincareService, UserRepository userRepository, OpenAIService openAIService) {
        this.skincareService = skincareService;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
    }

    @GetMapping(path = "/dashboard")
    public String dashboard(HttpServletRequest request, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid username. Please log in again.");
            return "error"; // Consider having a specific login error page or general error page with this message.
        }
        Long userId = user.get().getId();

        try {
            if (!skincareService.hasCompletedSurvey(userId)) {
                return "redirect:/survey";
            }

            Optional<SurveyResponse> surveyResponse = skincareService.getSurveyResponseByUserId(userId);
            if (surveyResponse.isEmpty()) {
                model.addAttribute("errorMessage", "Please complete the survey to get your personalized skincare routines.");
                return "redirect:/error";
            }

            if (!skincareService.hasAssignedRoutines(userId)) {
                String userQuery = generateUserQuery(surveyResponse.get());
                try {
                    String routine = openAIService.generateSkincareRoutine(userQuery);
                    skincareService.saveRoutine(routine, userId);
                } catch (Exception e) {
                    model.addAttribute("errorMessage", "Failed to generate skincare routine. Please try again later.");
                    return "redirect:/error";
                }
                return "redirect:/dashboard";
            }

            model.addAttribute("user", userDetails);
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            return "error";
        }
    }

    private String generateUserQuery(SurveyResponse surveyResponse) {
        return String.format("Create a skincare routine for someone with %s skin type, concerned about %s. " +
                        "Their skin %s to new skincare products. They are in the %s age range and are %s exposed to the sun without protection.",
                surveyResponse.getSkinType().toLowerCase(),
                surveyResponse.getMainSkinConcern().toLowerCase(),
                surveyResponse.getReactionToNewProducts().toLowerCase(),
                surveyResponse.getAgeRange().toLowerCase(),
                surveyResponse.getSunExposure().toLowerCase());
    }



    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername() + " is logging out.");

        request.getSession().invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping(path = "/survey")
    public String showSurveyForm(Model model) {
        model.addAttribute("surveyResponse", new SurveyResponse());
        return "survey";
    }

    @PostMapping(path = "/survey")
    public String processSurvey(@ModelAttribute SurveyResponse surveyResponse, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            surveyResponse.setUserId(user.get().getId());
            skincareService.saveSurveyResponse(surveyResponse);

            return "redirect:/dashboard";
        } else {
            return "survey";
        }
    }
}

