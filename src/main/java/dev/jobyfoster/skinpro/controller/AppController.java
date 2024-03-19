package dev.jobyfoster.skinpro.controller;

import com.azure.core.annotation.Get;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.SurveyResponse;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
import dev.jobyfoster.skinpro.service.OpenAIService;
import dev.jobyfoster.skinpro.service.SkincareService;
import dev.jobyfoster.skinpro.service.UserService;
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

import java.util.List;
import java.util.Optional;

@Controller
public class AppController {
    private final SkincareService skincareService;
    private final UserRepository userRepository;
    private final OpenAIService openAIService;
    private final UserService userService;

    // Autowired constructor for dependency injection, ensuring that Spring manages the lifecycle of the dependencies.
    @Autowired
    public AppController(SkincareService skincareService, UserRepository userRepository, OpenAIService openAIService, UserService userService) {
        this.skincareService = skincareService;
        this.userRepository = userRepository;
        this.openAIService = openAIService;
        this.userService = userService;
    }
    @GetMapping(path = "/")
    public String home(HttpServletRequest request, Model model, Authentication authentication) {
        return "home";
    }

    // Handles HTTP GET requests for the "/dashboard" path. It performs user authentication, checks survey completion,
    // and manages redirection to the survey or error pages based on the user's survey completion status.
    @GetMapping(path = "/dashboard")
    public String dashboard(HttpServletRequest request, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        // Redirects to an error page if the username does not exist in the database.
        if (user.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid username. Please log in again.");
            // TODO: Consider having a specific login error page or general error page with this message.
            return "error";
        }
        Long userId = user.get().getId();

        try {
            // Redirects to the survey page if the user has not completed the survey.
            if (!skincareService.hasCompletedSurvey(userId)) {
                return "redirect:/survey";
            }

            // Checks if a survey response exists for the user and redirects to an error page if not.
            Optional<SurveyResponse> surveyResponse = skincareService.getSurveyResponseByUserId(userId);
            if (surveyResponse.isEmpty()) {
                model.addAttribute("errorMessage", "Please complete the survey to get your personalized skincare routines.");
                return "redirect:/error";
            }

            // Generates and saves a personalized skincare routine if one has not been assigned yet.
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

            // Adds the UserDetails to the model and returns the "dashboard" view if no errors occur.
            List<SkincareRoutine> dayRoutine = skincareService.getDayRoutine(userId);
            List<SkincareRoutine> nightRoutine = skincareService.getNightRoutine(userId);
            userService.streakLogic(user);
            model.addAttribute("dayRoutine", dayRoutine);
            model.addAttribute("nightRoutine", nightRoutine);
            model.addAttribute("user", user.get());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            return "error";
        }
    }

    // Generates a user query for the OpenAI service based on the user's survey response.
    private String generateUserQuery(SurveyResponse surveyResponse) {
        return String.format("Create a skincare routine for someone with %s skin type, concerned about %s. " +
                        "Their skin %s to new skincare products. They are in the %s age range and are %s exposed to the sun without protection.",
                surveyResponse.getSkinType().toLowerCase(),
                surveyResponse.getMainSkinConcern().toLowerCase(),
                surveyResponse.getReactionToNewProducts().toLowerCase(),
                surveyResponse.getAgeRange().toLowerCase(),
                surveyResponse.getSunExposure().toLowerCase());
    }

    // Simple method to handle GET requests for the signup page, returning the "signup" view.
    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    // Handles GET requests for the login page, returning the "login" view.
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    // Handles GET requests for the logout functionality, invalidating the session and redirecting to the login page with a logout parameter.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername() + " is logging out.");
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }

    // Handles GET requests to display the survey form by adding an empty SurveyResponse to the model, returning the "survey" view.
    @GetMapping(path = "/survey")
    public String showSurveyForm(Model model) {
        model.addAttribute("surveyResponse", new SurveyResponse());
        return "survey";
    }

    // Processes POST requests for survey submission, saving the survey response and redirecting to the dashboard or back to the survey on failure.
    @PostMapping(path = "/survey")
    public String processSurvey(@ModelAttribute SurveyResponse surveyResponse, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            surveyResponse.setUserId(user.get().getId());
            skincareService.saveSurveyResponse(surveyResponse);
            return "redirect:/dashboard";
        } else {
            // This might indicate an error state; consider adding an error message or redirecting to an error page.
            return "survey";
        }
    }
}


