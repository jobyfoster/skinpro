package dev.jobyfoster.skinpro.controller;

import dev.jobyfoster.skinpro.model.SurveyResponse;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.UserRepository;
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

    @Autowired
    public AppController(SkincareService skincareService, UserRepository userRepository) {
        this.skincareService = skincareService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if(user.isEmpty()){
            model.addAttribute("errorMessage", "Invalid username.");
            return "error"; // Directly show the error page with a message
        }
        Long userId = user.get().getId();

        boolean surveyCompleted = skincareService.hasCompletedSurvey(userId);
        if (!surveyCompleted) {
            // Redirect or notify the user to complete the survey
            return "redirect:/survey"; // Adjust based on your survey page
        }

//        boolean routinesAssigned = skincareService.hasAssignedRoutines(userId);
//        if (!routinesAssigned) {
//            // Handle the case where no routines are assigned yet
//            model.addAttribute("errorMessage", "Please complete the survey to get your personalized skincare routines.");
//            return "redirect:/error"; // Adjust as needed
//        }

        model.addAttribute("user", userDetails);
        return "dashboard";
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

