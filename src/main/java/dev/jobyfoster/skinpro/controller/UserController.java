package dev.jobyfoster.skinpro.controller;

import com.azure.core.annotation.Get;
import dev.jobyfoster.skinpro.model.SkincareRoutine;
import dev.jobyfoster.skinpro.model.User;
import dev.jobyfoster.skinpro.repository.SkincareRoutineRepository;
import dev.jobyfoster.skinpro.repository.UserRepository;
import dev.jobyfoster.skinpro.service.SkincareService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final SkincareRoutineRepository skincareRoutineRepository;
    private final SkincareService skincareService;
    @GetMapping(path="/completed/day")
    public String completeDayRoutine(Authentication authentication, Model model){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if (user.isEmpty()){
                model.addAttribute("errorMessage", "Invalid Username. Please log in again.");
                return "error";
            }
            List<SkincareRoutine> dayRoutine = skincareService.getDayRoutine(user.get().getId());
            dayRoutine.getFirst().setLastCompleted(LocalDate.now());
            skincareRoutineRepository.save(dayRoutine.getFirst());
            return "dashboard";
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
        nightRoutine.getFirst().setLastCompleted(LocalDate.now());
        skincareRoutineRepository.save(nightRoutine.getFirst());
        return "dashboard";
    }
}
