package dev.jobyfoster.skinpro.controller;

import com.azure.core.annotation.Get;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.SkincareRoutineRepository;
import dev.jobyfoster.skinpro.repository.UserRepository;
import dev.jobyfoster.skinpro.service.SkincareService;
import dev.jobyfoster.skinpro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final SkincareRoutineRepository skincareRoutineRepository;
    private final SkincareService skincareService;
    private final UserService userService;
    @GetMapping(path="/completed/day")
    public String completeDayRoutine(Authentication authentication, Model model){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if (user.isEmpty()){
                model.addAttribute("errorMessage", "Invalid Username. Please log in again.");
                return "error";
            }
            List<SkincareRoutine> dayRoutine = skincareService.getDayRoutine(user.get().getId());
        if (!dayRoutine.isEmpty()) {
            SkincareRoutine firstDayRoutine = dayRoutine.get(0); // Use get(0) to access the first element
            firstDayRoutine.setLastCompleted(LocalDate.now());
            skincareRoutineRepository.save(firstDayRoutine);
        } else {
            // Handle the case where dayRoutine is empty
            model.addAttribute("errorMessage", "No day routine found.");
            return "dashboard"; // Or consider redirecting to another relevant page
        }

        userService.completeSkincareRoutine(user.get().getId());

        return "redirect:/dashboard";
    }

    @GetMapping(path="/completed/night")
    public String completeNightRoutine(Authentication authentication, Model model){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()){
            model.addAttribute("errorMessage", "Invalid Username. Please log in again.");
            return "error";
        }
        List<SkincareRoutine> nightRoutine = skincareService.getNightRoutine(user.get().getId());

        // Ensure the list is not empty to avoid IndexOutOfBoundsException
        if (!nightRoutine.isEmpty()) {
            SkincareRoutine firstNightRoutine = nightRoutine.get(0); // Use get(0) to access the first element
            firstNightRoutine.setLastCompleted(LocalDate.now());
            skincareRoutineRepository.save(firstNightRoutine);
        } else {
            // Handle the case where nightRoutine is empty
            model.addAttribute("errorMessage", "No night routine found.");
            return "dashboard"; // Or consider redirecting to another relevant page
        }
        userService.completeSkincareRoutine(user.get().getId());

        return "redirect:/dashboard"; // Use redirect to avoid form resubmission issues
    }

}
